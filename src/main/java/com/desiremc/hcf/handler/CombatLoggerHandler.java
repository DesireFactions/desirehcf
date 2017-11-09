package com.desiremc.hcf.handler;

import java.util.UUID;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.npc.SafeLogoutTask;
import com.desiremc.npc.HumanNPC;
import com.desiremc.npc.NPC;
import com.desiremc.npc.NPCLib;
import com.desiremc.npc.NPCPlayerHelper;
import com.desiremc.npc.NPCRegistry;
import com.desiremc.npc.events.NPCDespawnEvent;
import com.desiremc.npc.events.NPCDespawnEvent.NPCDespawnReason;

public class CombatLoggerHandler implements Listener
{
    private long TIMER = DesireHCF.getConfigHandler().getInteger("tag.time");

    private NPCRegistry reg = NPCLib.getNPCRegistry(DesireHCF.getInstance());

    public CombatLoggerHandler()
    {
        Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : TagHandler.getTaggedPlayers())
                {

                    if (TagHandler.getTagTime(uuid) == null)
                    {
                        TagHandler.clearTag(uuid);
                    }
                    Player p = PlayerUtils.getPlayer(uuid);
                    if (p != null)
                    {
                        EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().getStringNoPrefix("tag.scoreboard"), String.valueOf(TIMER - ((System.currentTimeMillis() - TagHandler.getTagTime(uuid)) / 1000)));
                    }
                }
            }
        }, 0, 10);
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        NPCPlayerHelper.removePlayerList(player);
        DesireHCF.getInstance().getPlayerCache().removePlayer(player);

        Session session = SessionHandler.getSession(player);

        if (session.getRank().isManager())
        {
            return;
        }

        UUID uuid = player.getUniqueId();
        Long time = TagHandler.getTagTime(uuid);

        if (time != null)
        {
            reg.createHumanNPC(uuid, player.getName()).spawn(player.getLocation());
        }
        else if (!SafeLogoutTask.isFinished(player))
        {
            int tagDistance = DesireHCF.getConfigHandler().getInteger("tag.distance");
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p == player)
                {
                    continue;
                }
                if (p.getLocation().distanceSquared(player.getLocation()) <= (tagDistance * tagDistance))
                {
                    HumanNPC npc = reg.createHumanNPC(uuid, player.getName());
                    npc.setSkin(uuid);
                    npc.spawn(player.getLocation());
                    break;
                }
            }
        }
    }

    @EventHandler
    public void despawnNPC(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        NPCPlayerHelper.createPlayerList(player);

        DesireHCF.getInstance().getPlayerCache().addPlayer(player);

        NPC npc = reg.getByUUID(event.getPlayer().getUniqueId());
        if (npc != null && npc.isSpawned())
        {
            npc.despawn(NPCDespawnReason.DESPAWN);
        }
    }

    @EventHandler
    public void despawnNPC(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        if (!reg.isNPC(player))
            return;

        final NPC npc = reg.getByUUID(player.getUniqueId());
        if (npc == null)
        {
            throw new IllegalStateException("Entity is NPC but could not retrieve by UUID.");
        }

        TagHandler.clearTag(player.getUniqueId());

        Bukkit.getScheduler().scheduleSyncDelayedTask(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                npc.despawn(NPCDespawnReason.DEATH);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void syncOffline(PlayerDeathEvent event)
    {
        // Do nothing if player is not a NPC
        final Player player = event.getEntity();
        if (!reg.isNPC(player))
            return;

        // NPC died, remove player's combat tag
        TagHandler.clearTag(player.getUniqueId());

        // Save NPC player data on next tick
        Bukkit.getScheduler().scheduleSyncDelayedTask(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                NPCPlayerHelper.syncOffline(player);
            }
        });
    }

    @EventHandler
    public void syncOffline(AsyncPlayerPreLoginEvent event)
    {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        final UUID playerId = event.getUniqueId();
        NPC npc = reg.getByUUID(playerId);
        if (npc == null)
            return;

        Bukkit.getScheduler().callSyncMethod(DesireHCF.getInstance(), new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {

                NPCPlayerHelper.syncOffline((Player) npc.getEntity());
                return null;
            }
        });
    }

    @EventHandler
    public void syncOffline(NPCDespawnEvent event)
    {
        NPC npc = event.getNPC();

        // Save player data when the NPC despawns
        Player player = DesireHCF.getInstance().getPlayerCache().getPlayer(npc.getUUID());
        if (player == null)
        {
            NPCPlayerHelper.syncOffline((Player) npc.getEntity());
            return;
        }
    }
}
