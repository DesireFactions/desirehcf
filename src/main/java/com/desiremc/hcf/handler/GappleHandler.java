package com.desiremc.hcf.handler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
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
                    EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().renderMessage("gapple.scoreboard", false, false));
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
                        EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().renderMessage("gapple.scoreboard", false, false), String.valueOf(TIMER - ((System.currentTimeMillis() - history.get(uuid)) / 1000)));
                    }
                }
            }
        }, 0, 10L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();

        if (event.getItem().getType() != Material.GOLDEN_APPLE)
        {
            return;
        }

        if (event.getItem().getDurability() != 1)
        {
            return;
        }

        UUID uuid = player.getUniqueId();
        Long time = history.get(uuid);

        if (time == null)
        {
            history.put(uuid, System.currentTimeMillis());

            Session s = SessionHandler.getSession(event.getPlayer());
            if (!s.hasAchievement(Achievement.FIRST_GAPPLE))
            {
                s.awardAchievement(Achievement.FIRST_GAPPLE, true);
            }
        }
        else
        {
            event.setCancelled(true);
            DesireHCF.getLangHandler().sendRenderMessage(player, "gapple.message", true, false, "{time}",
                    String.valueOf(TIMER - ((System.currentTimeMillis() - time) / 1000)));
        }
    }
}
