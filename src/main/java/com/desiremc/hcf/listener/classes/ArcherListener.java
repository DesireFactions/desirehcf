package com.desiremc.hcf.listener.classes;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.FactionRelationship;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArcherListener implements DesireClass
{

    private Cache<UUID, Long> archerHit;
    private int duration;

    public ArcherListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        duration = DesireHCF.getConfigHandler().getInteger("classes.archer.hit-time");
        archerHit = new Cache<>(duration, TimeUnit.SECONDS,
                new RemovalListener<UUID, Long>()
                {
                    @Override
                    public void onRemoval(RemovalNotification<UUID, Long> entry)
                    {
                        if (entry.getCause() != RemovalNotification.Cause.EXPIRE)
                        {
                            return;
                        }
                        Player p = PlayerUtils.getPlayer(entry.getKey());
                        if (p != null)
                        {
                            EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().getStringNoPrefix("classes.archer.scoreboard"));
                        }
                    }
                }, DesireHCF.getInstance());

        Bukkit.getScheduler().runTaskTimer(DesireCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : archerHit.keySet())
                {
                    Player p = Bukkit.getPlayer(uuid);
                    EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().getStringNoPrefix("classes.archer.scoreboard"), String.valueOf((duration - System.currentTimeMillis()) / 1000));
                }
            }
        }, 20, 20);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        if (event.getDamager() instanceof Arrow)
        {
            Arrow pj = (Arrow) event.getDamager();

            if (!(pj.getShooter() instanceof Player))
            {
                return;
            }

            Player target = (Player) event.getEntity();
            Player source = (Player) pj.getShooter();

            FSession sourceSession = FSessionHandler.getFSession(source.getUniqueId());

            if (!PVPClass.ARCHER.equals(sourceSession.getPvpClass()))
            {
                return;
            }

            if (archerHit.get(target.getUniqueId()) == null)
            {
                archerHit.put(target.getUniqueId(), System.currentTimeMillis());
            }
            else
            {
                archerHit.replace(target.getUniqueId(), System.currentTimeMillis());
            }
        }
        else if (event.getDamager() instanceof Player)
        {
            Player source = (Player) event.getDamager();
            Player target = (Player) event.getEntity();

            if (archerHit.get(target.getUniqueId()) == null)
            {
                return;
            }

            FactionRelationship rel = FactionsUtils.getFaction(source).getRelationshipTo(FactionsUtils.getFaction(target));

            if (rel == FactionRelationship.ALLY || rel == FactionRelationship.MEMBER)
            {
                return;
            }

            event.setDamage(event.getDamage() * DesireHCF.getConfigHandler().getDouble("classes.archer.increase-percent"));
        }
    }
}
