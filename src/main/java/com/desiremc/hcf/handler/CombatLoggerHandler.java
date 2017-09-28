package com.desiremc.hcf.handler;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.event.NPCDespawnEvent;
import com.desiremc.hcf.event.NPCDespawnReason;
import com.desiremc.hcf.npc.NPC;
import com.desiremc.hcf.npc.NPCManager;
import com.desiremc.hcf.npc.NPCPlayerHelper;
import com.desiremc.hcf.npc.SafeLogoutTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CombatLoggerHandler implements Listener
{
    private long TIMER = DesireCore.getConfigHandler().getInteger("tag.time");

    public CombatLoggerHandler()
    {

        Bukkit.getScheduler().runTaskTimerAsynchronously(HCFCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : TagHandler.getTaggedPlayers())
                {
                    EntryRegistry.getInstance().setValue(Bukkit.getPlayer(uuid), DesireCore.getLangHandler().getString("tag.scoreboard"),
                            String.valueOf(TIMER - ((System.currentTimeMillis() - TagHandler.tags.getIfPresent(uuid)) / 1000)));
                }
            }
        }, 0, 10);
    }

    @EventHandler
    public void onPlayerAttackPlayer(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player) && !(event.getEntity() instanceof Player)) return;

        Player p = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        TagHandler.tagPlayer(p, damager);
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        NPCPlayerHelper.removePlayerList(player);
        HCFCore.getInstance().getPlayerCache().removePlayer(player);

        if (player.hasPermission("combat.bypass") || player.isOp()) return;
        UUID uuid = player.getUniqueId();
        Long time = TagHandler.tags.getIfPresent(uuid);


        if (time != null)
        {
            NPCManager.spawn(player);
        }
        else
        {
            if(SafeLogoutTask.isFinished(player)) return;
            int tagDistance = DesireCore.getConfigHandler().getInteger("tag.distance")^2;
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getLocation().distanceSquared(player.getLocation()) <= tagDistance)
                {
                    NPCManager.spawn(player);
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

        HCFCore.getInstance().getPlayerCache().addPlayer(player);

        NPC npc = NPCManager.getSpawnedNPC(event.getPlayer().getUniqueId());
        if (npc != null)
        {
            NPCManager.despawn(npc);
        }
    }

    @EventHandler
    public void despawnNPC(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        if (!NPCPlayerHelper.isNpc(player)) return;

        UUID id = NPCPlayerHelper.getIdentity(player).getId();
        final NPC npc = NPCManager.getSpawnedNPC(id);
        if (npc == null) return;

        TagHandler.clearTag(id);

        Bukkit.getScheduler().scheduleSyncDelayedTask(HCFCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                NPCManager.despawn(npc, NPCDespawnReason.DEATH);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void syncOffline(PlayerDeathEvent event)
    {
        // Do nothing if player is not a NPC
        final Player player = event.getEntity();
        if (!NPCPlayerHelper.isNpc(player)) return;

        // NPC died, remove player's combat tag
        TagHandler.clearTag(player.getUniqueId());

        // Save NPC player data on next tick
        Bukkit.getScheduler().scheduleSyncDelayedTask(HCFCore.getInstance(), new Runnable()
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
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        final UUID playerId = event.getUniqueId();
        if (!NPCManager.NPCExists(playerId)) return;

        Future<?> future = Bukkit.getScheduler().callSyncMethod(HCFCore.getInstance(), new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                NPC npc = NPCManager.getSpawnedNPC(playerId);
                if (npc == null) return null;

                NPCPlayerHelper.syncOffline(npc.getEntity());
                return null;
            }
        });

        try
        {
            future.get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void syncOffline(NPCDespawnEvent event)
    {
        NPC npc = event.getNPC();

        // Save player data when the NPC despawns
        Player player = HCFCore.getInstance().getPlayerCache().getPlayer(npc.getIdentity().getId());
        if (player == null)
        {
            NPCPlayerHelper.syncOffline(npc.getEntity());
            return;
        }

        // Copy NPC player data to online player
        Player npcPlayer = npc.getEntity();
        player.setMaximumAir(npcPlayer.getMaximumAir());
        player.setRemainingAir(npcPlayer.getRemainingAir());
        player.setHealthScale(npcPlayer.getHealthScale());
        player.setMaxHealth(getRealMaxHealth(npcPlayer));
        player.setHealth(npcPlayer.getHealth());
        player.setTotalExperience(npcPlayer.getTotalExperience());
        player.setFoodLevel(npcPlayer.getFoodLevel());
        player.setExhaustion(npcPlayer.getExhaustion());
        player.setSaturation(npcPlayer.getSaturation());
        player.setFireTicks(npcPlayer.getFireTicks());
        player.getInventory().setContents(npcPlayer.getInventory().getContents());
        player.getInventory().setArmorContents(npcPlayer.getInventory().getArmorContents());
        player.addPotionEffects(npcPlayer.getActivePotionEffects());
    }

    private static double getRealMaxHealth(Player npcPlayer)
    {
        double health = npcPlayer.getMaxHealth();
        for (PotionEffect p : npcPlayer.getActivePotionEffects())
        {
            if (p.getType().equals(PotionEffectType.HEALTH_BOOST))
            {
                health -= (p.getAmplifier() + 1) * 4;
            }
        }
        return health;
    }
}
