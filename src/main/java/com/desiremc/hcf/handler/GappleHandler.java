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
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;

public class GappleHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public GappleHandler()
    {
        TIMER = DesireHCF.getConfigHandler().getInteger("gapple.time");
        history = new Cache<>(TIMER, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {

            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "gapple.ended");
                    EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().getStringNoPrefix("gapple.scoreboard"));
                }
            }
        }, DesireHCF.getInstance());

        Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : history.keySet())
                {
                    Player p = PlayerUtils.getPlayer(uuid);
                    if (p != null)
                    {
                        EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().getStringNoPrefix("gapple.scoreboard"), String.valueOf(TIMER - ((System.currentTimeMillis() - history.get(uuid)) / 1000)));
                    }
                }
            }
        }, 0, 10);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getPlayer().getItemInHand() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE)
        {
            UUID uuid = event.getPlayer().getUniqueId();
            Long time = history.get(uuid);

            if (time == null)
            {
                history.put(uuid, System.currentTimeMillis());
            }
            else
            {
                event.setCancelled(true);
                DesireHCF.getLangHandler().sendRenderMessage(event.getPlayer(), "gapple.message", "{time}", String.valueOf(TIMER - ((System.currentTimeMillis() - time) / 1000)));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();

        if (event.getItem().getType() != Material.GOLDEN_APPLE && event.getItem().getDurability() != 1)
            return;

        UUID uuid = player.getUniqueId();
        Long time = history.get(uuid);

        if (time == null)
        {
            history.put(uuid, System.currentTimeMillis());
        }
        else
        {
            event.setCancelled(true);
            DesireHCF.getLangHandler().sendRenderMessage(player, "gapple.message", "{time}", String.valueOf(TIMER - ((System.currentTimeMillis() - time) / 1000)));
        }
    }
}
