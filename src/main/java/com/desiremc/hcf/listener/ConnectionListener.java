package com.desiremc.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.desiremc.core.session.HCFSessionHandler;

public class ConnectionListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e)
    {
        HCFSessionHandler.initializeHCFSession(e.getPlayer().getUniqueId(), true);
    }
    
}
