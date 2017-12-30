package com.desiremc.hcf.handler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.FactionChannel;

public class SlowChatHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public SlowChatHandler()
    {
        TIMER = DesireCore.getConfigHandler().getInteger("staff.chat_slow");
        history = new Cache<>(TIMER, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
            }
        }, DesireCore.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(AsyncPlayerChatEvent event)
    {
        if (!StaffHandler.getInstance().isChatSlowed())
        {
            return;
        }

        Player player = event.getPlayer();
        FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());

        if (session.getRank().isStaff())
        {
            return;
        }

        if (session.getChannel() != FactionChannel.GENERAL)
        {
            return;
        }

        if (history.containsKey(player.getUniqueId()))
        {
            event.setCancelled(true);
            DesireCore.getLangHandler().sendRenderMessage(session, "staff.chat_slowed", true, false,
                    "{time}", DateUtils.formatDateDiff(history.get(player.getUniqueId())));
        }
        else
        {
            history.put(player.getUniqueId(), System.currentTimeMillis() + (TIMER * 1000));
        }
    }

}
