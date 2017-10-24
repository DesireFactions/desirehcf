package com.desiremc.hcf.listener.classes;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;

public class BardListener implements DesireClass
{

    private Cache<UUID, Long> cooldown;

    @Override
    public void initialize()
    {
        cooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.bard.instant-cooldown"), TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.bard.instant-cooldown-over");
                }
            }
        }, DesireHCF.getInstance());
    }

    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!session.getPvpClass().equals(PVPClass.BARD))
        {
            return;
        }

        if (cooldown.get(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendString(p, "classes.bard.on-cooldown");
            return;
        }

        switch (p.getItemInHand().getType())
        {
            case SPECKLED_MELON:
                for (Player target : PlayerUtils.getPlayersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.distance")))
                {

                }
                break;
            case WHEAT:
                for (Player target : PlayerUtils.getPlayersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.distance")))
                {

                }
                break;
            case EYE_OF_ENDER:
                for (Player target : PlayerUtils.getPlayersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.distance")))
                {

                }
                break;
        }

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
    }

}