package com.desiremc.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.core.staff.StaffHandler;

public class InteractListener implements Listener
{

    private static final StaffHandler STAFF = StaffHandler.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {

        if (STAFF.inStaffMode(e.getPlayer()))
        {
            //STAFF.playerInteract(e);
        }

        if (STAFF.runningCPSTests())
        {
            STAFF.handleCPSTest(e);
        }

    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e)
    {
        if (STAFF.inStaffMode(e.getPlayer()))
        {
            //STAFF.playerInteractEntity(e);
        }
    }

}
