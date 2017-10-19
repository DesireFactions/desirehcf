package com.desiremc.hcf.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.barrier.TagHandler.Tag;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.util.FactionsUtils;

public class CombatListener implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHitMonitor(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)
            {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager()).getShooter() : e.getDamager());
                TagHandler.tagPlayer(victim, damager);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHitHigh(EntityDamageByEntityEvent e)
    {
        LangHandler lang = DesireHCF.getLangHandler();
        if (e.getEntity() instanceof Player)
        {
            if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)
            {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager()).getShooter() : e.getDamager());

                HCFSession vs = HCFSessionHandler.getHCFSession(victim);
                HCFSession ds = HCFSessionHandler.getHCFSession(damager);

                if (ds.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    damager.sendMessage(lang.getString("pvp.damager_protected"));
                    return;
                }

                if (vs.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    damager.sendMessage(lang.getString("pvp.target_protected"));
                    return;
                }

                // 0 = valid, 1 = damager in region, 2 = victim in region
                int state = 0;
                for (Region r : RegionHandler.getInstance().getRegions())
                {
                    if (r.getRegion().isWithin(damager.getLocation()))
                    {
                        state = 1;
                        break;
                    }
                    else if (r.getRegion().isWithin(victim.getLocation()))
                    {
                        state = 2;
                        break;
                    }
                }
                if (state != 0)
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        try
        {
            Player vPlayer = event.getEntity();
            HCFSession victim = HCFSessionHandler.getHCFSession(vPlayer);
            DamageCause cause = vPlayer.getLastDamageCause().getCause();

            String parsed;

            Tag tag = TagHandler.getTag(vPlayer.getUniqueId());
            if (tag != null)
            {
                Player kPlayer = Bukkit.getPlayer(tag.getUniqueId());
                HCFSession killer = HCFSessionHandler.getHCFSession(kPlayer);

                killer.addKill(DesireCore.getCurrentServer());
                HCFSessionHandler.getInstance().save(killer);

                parsed = DesireHCF.getLangHandler().getString("death.pvp." + cause);

                parsed = ChatUtils.renderString(parsed,
                        "{killer}", killer.getName(),
                        "{killerKills}", killer.getKills(DesireCore.getCurrentServer()) + "");

            }
            else
            {
                parsed = DesireHCF.getLangHandler().getString("death.pve." + cause);
            }

            if (parsed.contains("death.pvp."))
            {
                parsed = DesireHCF.getLangHandler().getString("death.pvp.default");
            }
            else if (parsed.contains("death.pve."))
            {
                parsed = DesireHCF.getLangHandler().getString("death.pve.default");
            }

            parsed = ChatUtils.renderString(parsed,
                    "{victim}", vPlayer.getName(),
                    "{victimKills}", String.valueOf(victim.getKills(DesireCore.getCurrentServer())));

            if (parsed == null)
            {
                System.out.println("===NULLL===");
            }
            else
            {
                System.out.println("===" + parsed + "===");
            }
            FancyMessage message = new FancyMessage();
            String[] pieces = parsed.split("\\{item\\}");
            if (pieces.length == 1)
            {
                message.then(pieces[0]);
            }
            else if (pieces.length == 2)
            {
                message.then(pieces[0]);
                message.then(ItemNames.lookup(tag.getItem())).itemTooltip(tag.getItem());
                message.then(pieces[1]);
            }
            else
            {
                throw new IllegalStateException("{item} can only be used once.");
            }

            for (Player online : Bukkit.getOnlinePlayers())
            {
                message.send(online);
            }
            System.out.println(message.toJSONString());
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
                .then("[" + session.getKills(DesireCore.getCurrentServer()) + "]");

        return message;
    }

}
