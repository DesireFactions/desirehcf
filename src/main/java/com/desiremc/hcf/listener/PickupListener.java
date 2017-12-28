package com.desiremc.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;

public class PickupListener implements Listener
{

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event)
    {
        Session session = SessionHandler.getOnlineSession(event.getPlayer().getUniqueId());
        if (session.getSetting(SessionSetting.COBBLE))
        {
            event.setCancelled(true);
        }
    }

}
