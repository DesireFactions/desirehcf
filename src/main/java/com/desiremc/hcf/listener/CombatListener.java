package com.desiremc.hcf.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.api.LangHandler;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;

public class CombatListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onHitMonitor(EntityDamageByEntityEvent e) {
        if (e.isCancelled() == true) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)) {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager()).getShooter() : e.getDamager());
                TagHandler.tagPlayer(victim, damager);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHitHigh(EntityDamageByEntityEvent e) {
        LangHandler l = DesireCore.getLangHandler();
        if (e.isCancelled() == true) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)) {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager()).getShooter() : e.getDamager());

                Session vs = SessionHandler.getSession(victim);
                Session ds = SessionHandler.getSession(damager);

                if (ds.getSafeTimeLeft() > 0) {
                    e.setCancelled(true);
                    damager.sendMessage(l.getString("pvp.damager_protected"));
                    return;
                }

                if (vs.getSafeTimeLeft() > 0) {
                    e.setCancelled(true);
                    damager.sendMessage(l.getString("pvp.target_protected"));
                    return;
                }

                // 0 = valid, 1 = damager in region, 2 = victim in region
                int state = 0;
                for (Region r : RegionHandler.getInstance().getRegions()) {
                    if (r.getRegion().isWithin(damager.getLocation())) {
                        state = 1;
                        break;
                    } else if (r.getRegion().isWithin(victim.getLocation())) {
                        state = 2;
                        break;
                    }
                }
                if (state != 0) {
                    e.setCancelled(true);
                }
                if (state == 1) {
                    damager.sendMessage(l.getString("damaged.in-spawn"));
                } else if (state == 2) {
                    damager.sendMessage(l.getString("damaged.out-spawn"));
                }
            }
        }
    }
}
