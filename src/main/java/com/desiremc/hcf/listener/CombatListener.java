package com.desiremc.hcf.listener;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.utils.BungeeUtils;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.barrier.TagHandler.Tag;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
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
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager())
                        .getShooter() : e.getDamager());
                TagHandler.tagPlayer(victim, damager);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent e)
    {

        if(!(e.getEntity() instanceof Player))
        {
            return;
        }

        if(!(e.getDamager() instanceof Player))
        {
            return;
        }

        Player victim = (Player) e.getEntity();

        if (e.getEntity() instanceof Player && !npcRegistry.isNPC(victim))
        {
            if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile && !(e.getDamager() instanceof EnderPearl) && ((Projectile) e.getDamager()).getShooter() instanceof Player)
            {
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager())
                        .getShooter() : e.getDamager());

                // 0 = valid, 1 = damager in region, 2 = victim in region
                int state = 0;
                for (Region r : RegionHandler.getInstance().getRegions())
                {
                    if (r.getRegion().isWithin(damager.getLocation()))
                    {
                        DesireHCF.getLangHandler().sendRenderMessage(damager, "pvp.damager_safe");
                        state = 1;
                        break;
                    }
                    else if (r.getRegion().isWithin(victim.getLocation()))
                    {
                        DesireHCF.getLangHandler().sendRenderMessage(damager, "pvp.victim_zone");
                        state = 2;
                        break;
                    }
                }
                if (state != 0)
                {
                    e.setCancelled(true);
                    return;
                }

                HCFSession vs = HCFSessionHandler.getHCFSession(victim.getUniqueId());
                HCFSession ds = HCFSessionHandler.getHCFSession(damager.getUniqueId());

                if (ds.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    damager.sendMessage(DesireHCF.getLangHandler().getString("pvp.damager_protected"));
                    return;
                }

                if (vs.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    damager.sendMessage(DesireHCF.getLangHandler().getString("pvp.target_protected"));
                }
            }
        }
    }

    @EventHandler()
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        try
        {
            Player vPlayer = event.getEntity();
            HCFSession victim = HCFSessionHandler.getHCFSession(vPlayer);
            DamageCause cause = vPlayer.getLastDamageCause().getCause();

            Tag tag = TagHandler.getTag(vPlayer.getUniqueId());
            if (tag != null)
            {
                Player kPlayer = PlayerUtils.getPlayer(tag.getUniqueId());
                HCFSession killer = HCFSessionHandler.getHCFSession(kPlayer);

                killer.addKill(vPlayer.getUniqueId());
                HCFSessionHandler.getInstance().save(killer);
            }

            UUID killer = tag == null ? cause != DamageCause.CUSTOM ? cause != DamageCause.SUICIDE ? null : vPlayer.getUniqueId() : DesireCore.getConsoleUUID() : tag.getUniqueId();
            victim.addDeath(killer);
            BungeeUtils.sendToHub(vPlayer);

            FancyMessage message = processMessage(victim, cause, tag);

            for (Session s : SessionHandler.getInstance().getSessions())
            {
                if (s.getSetting(SessionSetting.DEATH))
                {
                    message.send(s.getPlayer());
                }
            }
        }
        catch (Exception ex)
        {
            ChatUtils.sendStaffMessage(ex, DesireHCF.getInstance());
        }
        event.setDeathMessage(null);
    }

    private FancyMessage processMessage(HCFSession session, DamageCause cause, Tag tag)
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
            HCFSession killer = HCFSessionHandler.getHCFSession(tag.getUniqueId());
            message.then(killer.getName())
                    .tooltip(FactionsUtils.getMouseoverDetails(killer))
                    .color(killer.getRank().getMain())
                    .then("[")
                    .color(ChatColor.DARK_RED)
                    .then(Integer.toString(killer.getTotalKills()))
                    .tooltip(killer.getKillDisplay())
                    .color(ChatColor.RED)
                    .then("]")
                    .color(ChatColor.DARK_RED)
                    .then(" using ")
                    .color(ChatColor.WHITE)
                    .then(ItemNames.lookup(tag.getItem()))
                    .itemTooltip(tag.getItem())
                    .color(ChatColor.BLUE)
                    .then(".")
                    .color(ChatColor.WHITE);
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

}
