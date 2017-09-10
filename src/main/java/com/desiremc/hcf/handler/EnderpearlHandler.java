package com.desiremc.hcf.handler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.hcf.Core;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class EnderpearlHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public EnderpearlHandler()
    {
        TIMER = Core.getConfigHandler().getInteger("enderpearl.time");
        history = CacheBuilder.newBuilder().expireAfterWrite(TIMER, TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Long>()
        {

            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Core.getLangHandler().sendString(Bukkit.getPlayer(entry.getKey()), "enderpearl.ended");
            }
        }).build();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();

        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (p.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL)
        {

            UUID uuid = p.getUniqueId();
            Long time = history.getIfPresent(uuid);

            if (time == null)
            {
                history.put(uuid, System.currentTimeMillis());
            } else
            {
                e.setCancelled(true);
                Core.getLangHandler().sendRenderMessage(p, "enderpearl.message", "{time}", String.valueOf(TIMER - ((System.currentTimeMillis() - time) / 1000)));
            }
        }
    }

}
