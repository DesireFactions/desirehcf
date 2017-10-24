package com.desiremc.hcf.handler;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EnderpearlHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public EnderpearlHandler()
    {
        TIMER = DesireHCF.getConfigHandler().getInteger("enderpearl.time");
        history = new Cache<>(TIMER, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "enderpearl.ended");
                    EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().getString("enderpearl.scoreboard"));
                }
            }

        }, DesireHCF.getInstance());

        Bukkit.getScheduler().runTaskTimerAsynchronously(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : history.keySet())
                {
                    Player p = Bukkit.getPlayer(uuid);
                    EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().getString("enderpearl.scoreboard"), String.valueOf(TIMER - ((System.currentTimeMillis() - history.get(uuid)) / 1000)));
                }
            }
        }, 0, 10);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();

        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (p.getInventory().getItemInHand().getType() == Material.ENDER_PEARL)
        {
            UUID uuid = p.getUniqueId();
            Long time = history.get(uuid);

            if (time == null)
            {
                history.put(uuid, System.currentTimeMillis());
            }
            else
            {
                e.setCancelled(true);
                DesireHCF.getLangHandler().sendRenderMessage(p, "enderpearl.message", "{time}", String.valueOf(TIMER - ((System.currentTimeMillis() - time) / 1000)));
            }
        }
    }

}
