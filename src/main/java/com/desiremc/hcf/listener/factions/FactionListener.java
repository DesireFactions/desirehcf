package com.desiremc.hcf.listener.factions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import com.desiremc.hcf.commands.factions.FactionHomeCommand;
import com.desiremc.hcf.events.faction.FactionLeaveEvent;

public class FactionListener implements Listener
{

    @EventHandler
    public void onFactionLeave(FactionLeaveEvent event)
    {
        BukkitTask task = FactionHomeCommand.getTeleportTask(event.getFSession().getUniqueId());
        if (task != null)
        {
            task.cancel();
        }
    }
    
}
