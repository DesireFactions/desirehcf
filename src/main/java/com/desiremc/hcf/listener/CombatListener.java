package com.desiremc.hcf.listener;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.BungeeUtils;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.barrier.TagHandler.Tag;
import com.desiremc.hcf.handler.SOTWHandler;
import com.desiremc.hcf.handler.SpawnHandler;
import com.desiremc.hcf.handler.TablistHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;
import com.desiremc.npc.NPCLib;
import com.desiremc.npc.NPCRegistry;

public class CombatListener implements Listener
{

    private NPCRegistry npcRegistry = NPCLib.getNPCRegistry(DesireHCF.getInstance());

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHitMonitor(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile && ((Projectile) e
                    .getDamager()).getShooter() instanceof Player)
            {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager()).getShooter() : e.getDamager());

                if (!victim.getName().equalsIgnoreCase(damager.getName()))
                {
                    TagHandler.tagPlayer(victim, damager);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event)
    {
        Player player = event.getEntity().getKiller();

        if (event.getEntity() instanceof Creeper)
        {
            Session s = SessionHandler.getSession(player);
            if (!s.hasAchievement(Achievement.FIRST_CREEPER))
            {
                s.awardAchievement(Achievement.FIRST_CREEPER, true);
            }
        }
        else if (event.getEntity() instanceof Enderman)
        {
            Session s = SessionHandler.getSession(player);
            if (!s.hasAchievement(Achievement.FIRST_ENDERMAN))
            {
                s.awardAchievement(Achievement.FIRST_ENDERMAN, true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent e)
    {
        if (!(e.getEntity() instanceof Player))
        {
            return;
        }

        if (!(e.getDamager() instanceof Player))
        {
            return;
        }

        Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager())
                .getShooter() : e.getDamager());

        if (SOTWHandler.getSOTW())
        {
            e.setCancelled(true);
            DesireHCF.getLangHandler().sendRenderMessage(damager, "sotw.pvp", true, false);
            return;
        }

        FSession p = FSessionHandler.getFSession((Player) e.getEntity());
        FSession k = FSessionHandler.getFSession((Player) e.getDamager());

        if (!p.getFaction().getRelationshipTo(k.getFaction()).canAttack())
        {
            e.setCancelled(true);
            return;
        }

        Player victim = (Player) e.getEntity();

        if (!npcRegistry.isNPC(victim))
        {
            if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile && !(e.getDamager() instanceof EnderPearl) && ((Projectile) e.getDamager()).getShooter() instanceof Player)
            {
                if (StaffHandler.getInstance().isFrozen(damager) || StaffHandler.getInstance().isFrozen(victim))
                {
                    e.setCancelled(true);
                    return;
                }

                // 0 = valid, 1 = damager in region, 2 = victim in region
                int state = 0;
                for (Region region : RegionHandler.getRegions())
                {
                    if (region.getRegionBlocks().isWithin(damager.getLocation()))
                    {
                        DesireHCF.getLangHandler().sendRenderMessage(damager, "pvp.damager_safe", true, false);
                        state = 1;
                        break;
                    }
                    else if (region.getRegionBlocks().isWithin(victim.getLocation()))
                    {
                        DesireHCF.getLangHandler().sendRenderMessage(damager, "pvp.victim_safe", true, false);
                        state = 2;
                        break;
                    }
                }
                if (state != 0)
                {
                    e.setCancelled(true);
                    return;
                }

                FSession vs = FSessionHandler.getOnlineFSession(victim.getUniqueId());
                FSession ds = FSessionHandler.getOnlineFSession(damager.getUniqueId());

                if (ds.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    DesireHCF.getLangHandler().sendRenderMessage(damager, "pvp.damager_protected", true, false);
                    return;
                }

                if (vs.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    DesireHCF.getLangHandler().sendRenderMessage(damager, "pvp.target_protected", true, false);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event)
    {
        event.setRespawnLocation(SpawnHandler.getInstance().getSpawn());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        try
        {
            long last = System.currentTimeMillis();
            // retrieve all variables we need
            Player vPlayer = event.getEntity();
            FSession victim = FSessionHandler.getFSession(vPlayer);
            DamageCause cause = vPlayer.getLastDamageCause().getCause();
            Tag tag = TagHandler.getTag(vPlayer.getUniqueId());
            TagHandler.clearTag(vPlayer.getUniqueId());

            System.out.println("Retrieving variables: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // get rid of the dead player
            BungeeUtils.sendToHub(vPlayer);

            System.out.println("Sending to hub: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // get the killer uuid. Can be the victim, their tagger, the console, or no one
            final UUID killer = tag == null ? cause != DamageCause.CUSTOM ? cause != DamageCause.SUICIDE ? null : vPlayer.getUniqueId() : DesireCore.getConsoleUUID() : tag.getUniqueId();

            // add the death and give them a new pvp timer
            victim.addDeath(killer);
            victim.resetPVPTimer();

            System.out.println("Add death, reset timer, get killer: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // check if they have a faction
            if (victim.hasFaction())
            {
                // if they do, decrease their dtr
                Faction faction = victim.getFaction();
                faction.setDTR(faction.getDTR() - 1);
                if (faction.getDTR() < faction.getMinDTR())
                {
                    faction.setDTR(faction.getMinDTR());
                }

                // also set their last death time
                faction.setLastDeathTime(System.currentTimeMillis());

                // save them too
                faction.save();
            }

            System.out.println("Update victim fact: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // if the killer was a player, give them their reward
            FSession kSession = getKillerSession(tag);
            if (kSession != null)
            {
                // if they have a faction, add the kill stat
                if (kSession.hasFaction())
                {
                    kSession.getFaction().addKill();
                    kSession.getFaction().save();
                }

                // add the kill stat to the player
                kSession.addKill(vPlayer.getUniqueId());

                // update a player's kills
                TablistHandler.updateKills(kSession);

                // give em an achievement
                if (!kSession.hasAchievement(Achievement.FIRST_KILL))
                {
                    kSession.awardAchievement(Achievement.FIRST_KILL, true);
                }
                kSession.save();
            }

            System.out.println("Update killer: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // update the database
            victim.save();

            System.out.println("Save victim: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // send the death message to everyone
            FancyMessage message = processMessage(victim, cause, tag);
            for (Session s : SessionHandler.getOnlineSessions())
            {
                if (s.getSetting(SessionSetting.DEATH))
                {
                    message.send(s.getPlayer());
                }
            }

            System.out.println("Send fancy to server: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

            // console wants to know too
            DesireHCF.getInstance().getLogger().info(message.toOldMessageFormat());

            System.out.println("Send fancy to console: " + (System.currentTimeMillis() - last) + "ms");
            last = System.currentTimeMillis();

        }
        catch (Exception ex)
        {
            ChatUtils.sendStaffMessage(ex, DesireHCF.getInstance());
        }

        // already sent a fancy message, cancel default
        event.setDeathMessage(null);
    }

    private FancyMessage processMessage(FSession session, DamageCause cause, Tag tag)
    {
        FancyMessage message = new FancyMessage(session.getName())
                .color(session.getRank().getMain())
                .tooltip(FactionsUtils.getMouseoverDetails(session))
                .then("[" + session.getTotalKills() + "]")
                .tooltip(session.getKillDisplay());

        String parsed = DesireHCF.getConfigHandler().getString("death." + (tag == null ? "pve." : "pvp.") + cause
                .toString().toLowerCase());
        if (parsed.contains("death.pvp."))
        {
            parsed = DesireHCF.getConfigHandler().getString("death.pvp.default");
        }
        else if (parsed.contains("death.pve."))
        {
            parsed = DesireHCF.getConfigHandler().getString("death.pve.default");
        }

        processFancyMessage(message, parsed);

        if (tag != null)
        {
            FSession killer = FSessionHandler.getOnlineFSession(tag.getUniqueId());
            if (tag.getItem() != null)
            {
                message.then(killer.getName())
                        .tooltip(FactionsUtils.getMouseoverDetails(killer))
                        .color(killer.getRank().getMain())
                        .then("[")
                        .color(ChatColor.WHITE)
                        .then(Integer.toString(killer.getTotalKills()))
                        .tooltip(killer.getKillDisplay())
                        .color(ChatColor.WHITE)
                        .then("]")
                        .color(ChatColor.WHITE)
                        .then(" using ")
                        .color(ChatColor.WHITE)
                        .then(ItemNames.lookup(tag.getItem()))
                        .itemTooltip(tag.getItem())
                        .color(ChatColor.BLUE)
                        .then(".")
                        .color(ChatColor.WHITE);
            }
            else
            {
                message.then(killer.getName())
                        .tooltip(FactionsUtils.getMouseoverDetails(killer))
                        .color(killer.getRank().getMain())
                        .then("[")
                        .color(ChatColor.WHITE)
                        .then(Integer.toString(killer.getTotalKills()))
                        .tooltip(killer.getKillDisplay())
                        .color(ChatColor.WHITE)
                        .then("]")
                        .color(ChatColor.WHITE)
                        .then(" using ")
                        .color(ChatColor.WHITE)
                        .then(ItemNames.lookup(tag.getItem()))
                        .color(ChatColor.BLUE)
                        .then(".")
                        .color(ChatColor.WHITE);
            }
        }

        return message;
    }

    private FancyMessage processFancyMessage(FancyMessage message, String string)
    {
        if (string == null || string.length() <= 1 || string.length() == 2 && string.matches("[&][0-9a-fA-Fk-oK-OrR]"))
        {
            return message;
        }
        if (!string.contains("&"))
        {
            return message.then(string);
        }

        String[] pieces = string.split("&(?=[0-9a-fA-Fk-oK-OrR])");
        ChatColor color;

        for (int i = 0; i < pieces.length; i++)
        {
            message.then();
            if (pieces[i].length() == 1)
            {
                if (i == pieces.length - 1)
                {
                    break;
                }
                if (!pieces[i].equalsIgnoreCase("r"))
                {
                    color = ChatColor.getByChar(pieces[i]);
                    if (pieces[i].matches("[0-9a-fA-F]"))
                    {
                        message.color(color);
                    }
                    else if (pieces[i].matches("[k-oK-O]"))
                    {
                        message.style(color);
                    }
                    i++;
                }
            }

            color = ChatColor.getByChar(pieces[i].charAt(0));
            if (pieces[i].matches("[0-9a-fA-F].*"))
            {
                message.color(color);
            }
            else if (pieces[i].matches("[k-oK-O].*"))
            {
                message.style(color);
            }
            else
            {
                message.then();
            }
            message.text(pieces[i].substring(1, pieces[i].length()));
        }
        return message;
    }

    private static FSession getKillerSession(Tag tag)
    {
        if (tag == null)
        {
            return null;
        }
        return FSessionHandler.getOnlineFSession(tag.getUniqueId());
    }

}
