package com.desiremc.hcf.handler;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.hcf.HCFCore;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GappleHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public GappleHandler()
    {
        TIMER = HCFCore.getConfigHandler().getInteger("gapple_time");
        history = CacheBuilder.newBuilder().expireAfterWrite(TIMER, TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Long>()
        {

            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    HCFCore.getLangHandler().sendString(p, "gapple.ended");
                    EntryRegistry.getInstance().removeValue(p, HCFCore.getLangHandler().getString("gapple.scoreboard"));
                }
            }
        }).build();

        Bukkit.getScheduler().runTaskTimerAsynchronously(HCFCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : history.asMap().keySet())
                {
                    Player p = Bukkit.getPlayer(uuid);
                    EntryRegistry.getInstance().setValue(p, HCFCore.getLangHandler().getString("gapple.scoreboard"),
                            String.valueOf(TIMER - ((System.currentTimeMillis() - history.getIfPresent(uuid)) / 1000)));
                }
            }
        }, 0, 10);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();

        if (event.getItem().getType() != Material.GOLDEN_APPLE && event.getItem().getDurability() != 1) return;

        UUID uuid = player.getUniqueId();
        Long time = history.getIfPresent(uuid);

        if (time == null)
        {
            history.put(uuid, System.currentTimeMillis());
        }
        else
        {
            event.setCancelled(true);
            HCFCore.getLangHandler().sendRenderMessage(player, "gapple.message", "{time}",
                    String.valueOf(TIMER - ((System.currentTimeMillis() - time) / 1000)));
        }
    }
}
