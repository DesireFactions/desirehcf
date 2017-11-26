package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArcherListener implements DesireClass
{

    private Cache<UUID, UUID> archerHit;

    public ArcherListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        archerHit = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.archer.hit-time"), TimeUnit.SECONDS,
                new RemovalListener<UUID, UUID>()
                {
                    @Override
                    public void onRemoval(RemovalNotification<UUID, UUID> entry)
                    {
                        if (entry.getCause() != RemovalNotification.Cause.EXPIRE)
                        {
                            return;
                        }
                        Player p = PlayerUtils.getPlayer(entry.getKey());
                        if (p != null)
                        {
                            DesireHCF.getLangHandler().sendString(p, "classes.archer.hit-off");
                        }
                    }
                }, DesireHCF.getInstance());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
        {
            return;
        }

        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        Player source = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (archerHit.get(target.getUniqueId()) == null)
        {
            return;
        }

        if (FactionsUtils.getFaction(source) == FactionsUtils.getFaction(archerHit.get(target.getUniqueId())))
        {
            return;
        }

        event.setDamage(event.getDamage() * DesireHCF.getConfigHandler().getDouble("classes.archer.increase-percent"));
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event)
    {

        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

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

        HCFSession sourceSession = HCFSessionHandler.getHCFSession(source.getUniqueId());

        if (!PVPClass.ARCHER.equals(sourceSession.getPvpClass()))
        {
            return;
        }

        int range = DesireHCF.getConfigHandler().getInteger("classes.archer.range");

        if (source.getLocation().distanceSquared(target.getLocation()) < (range * range))
        {
            return;
        }

        if (archerHit.get(target.getUniqueId()) != null)
        {
            archerHit.put(target.getUniqueId(), source.getUniqueId());
        }
        else
        {
            archerHit.replace(target.getUniqueId(), source.getUniqueId());
        }
    }
}
