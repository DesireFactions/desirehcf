package com.desiremc.hcf.listener;

import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHitMonitor(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            if (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player))
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
        LangHandler l = DesireCore.getLangHandler();
        if (e.getEntity() instanceof Player)
        {
            if (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player))
            {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) (e.getDamager() instanceof Projectile ? ((Projectile) e.getDamager()).getShooter() : e.getDamager());

                HCFSession vs = HCFSessionHandler.getHCFSession(victim);
                HCFSession ds = HCFSessionHandler.getHCFSession(damager);

                if (ds.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    damager.sendMessage(l.getString("pvp.damager_protected"));
                    return;
                }

                if (vs.getSafeTimeLeft() > 0)
                {
                    e.setCancelled(true);
                    damager.sendMessage(l.getString("pvp.target_protected"));
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
                else if (state == 1)
                {
                    damager.sendMessage(l.getString("damaged.in-spawn"));
                }
                else if (state == 2)
                {
                    damager.sendMessage(l.getString("damaged.out-spawn"));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        if (player.getKiller() == null || !(player.getKiller() instanceof Player)) return;

        HCFSession killer = HCFSessionHandler.getHCFSession(player.getKiller());
        HCFSession playerSession = HCFSessionHandler.getHCFSession(player);

        killer.addKill(DesireCore.getCurrentServer());

        ItemStack item = killer.getPlayer().getInventory().getItemInMainHand();
        String itemType;

        if (item != null && item.getType() != Material.AIR)
        {
            itemType = WordUtils.capitalize(item.getType().name().replace("_", " "));
        }
        else
        {
            itemType = "Fist";
        }

        String parsed = DesireCore.getLangHandler().renderMessage("pvp.kill", "{killer}", killer.getName(), "{player}",
                player.getName(), "{killerKills}", killer.getKills(DesireCore.getCurrentServer()) + "", "{playerKills}", playerSession.getKills(DesireCore.getCurrentServer()) + "", "{item}", itemType);

        for (Player online : Bukkit.getOnlinePlayers())
        {
            new FancyMessage(parsed)
                    .itemTooltip(item)
                    .send(online);
        }
    }
}
