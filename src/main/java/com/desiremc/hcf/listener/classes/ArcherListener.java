package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ArcherListener implements DesireClass
{

    @Override
    public void initialize()
    {
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Arrow))
            return;

        Projectile pj = (Projectile) event.getDamager();

        if (!(pj.getShooter() instanceof Player))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player target = (Player) event.getEntity();
        Player source = (Player) pj.getShooter();

        HCFSession sourceSession = HCFSessionHandler.getHCFSession(source.getUniqueId());

        if (!sourceSession.getPvpClass().equals(PVPClass.ARCHER))
            return;

    }
}
