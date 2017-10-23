package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.hcf.DesireHCF;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BardListener implements Listener
{

    private Cache<UUID, Long> cooldown;

    public BardListener()
    {
        cooldown = CacheBuilder.newBuilder().expireAfterWrite(DesireHCF.getConfigHandler().getInteger("classes.bard.instant-cooldown"),
                TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Long>()
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
        }).build();
    }

    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!session.getPvpClass().equals(PVPClass.BARD)) return;

        if (cooldown.getIfPresent(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendString(p, "classes.bard.on-cooldown");
            return;
        }

        switch (p.getInventory().getItemInMainHand().getType())
        {
            case SPECKLED_MELON:
                for (Player target : getPlayersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.distance")))
                {

                }
                break;
            case WHEAT:
                for (Player target : getPlayersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.distance")))
                {

                }
                break;
            case EYE_OF_ENDER:
                for (Player target : getPlayersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.distance")))
                {

                }
                break;
        }

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
    }

    private List<Player> getPlayersInRange(Player p, int range)
    {
        List<Player> inRange = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getLocation().distanceSquared(p.getLocation()) <= (range * range))
            {
                inRange.add(player);
            }
        }

        return inRange;
    }
}