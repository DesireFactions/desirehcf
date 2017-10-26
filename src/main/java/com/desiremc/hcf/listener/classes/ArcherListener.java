package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArcherListener implements DesireClass
{

    private Cache<UUID, Long> archerHit;

    @Override
    public void initialize()
    {
        archerHit = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.archer.hit-time"), TimeUnit
                .SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.archer.hit-off");
                }
            }
        }, DesireHCF.getInstance());
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event)
    {

        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Arrow))
        {
            return;
        }

        Arrow pj = (Arrow) event.getDamager();

        if (!(pj.getShooter() instanceof Player))
        {
            return;
        }

        Player target = (Player) event.getEntity();
        Player source = (Player) pj.getShooter();

        int range = DesireHCF.getConfigHandler().getInteger("classes.archer.range");

        if (source.getLocation().distanceSquared(target.getLocation()) < (range * range))
            return;

        HCFSession sourceSession = HCFSessionHandler.getHCFSession(source.getUniqueId());

        if (!sourceSession.getPvpClass().equals(PVPClass.ARCHER))
            return;

        if (archerHit.get(target.getUniqueId()) != null)
        {
            event.setDamage(event.getDamage() * DesireHCF.getConfigHandler().getDouble("classes.archer" +
                    ".increase-percent"));
        }
        else
        {
            archerHit.put(target.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!event.getItem().getType().equals(Material.SUGAR))
            return;

        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!session.getPvpClass().equals(PVPClass.ARCHER))
            return;

        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, DesireHCF.getConfigHandler().getInteger
                ("classes.archer.speed.duration"),
                DesireHCF.getConfigHandler().getInteger("classes.archer.speed.amplifier"));

        p.addPotionEffect(effect);

    }
}
