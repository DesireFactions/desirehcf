package com.desiremc.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.hcf.session.StaffHandler;

public class InteractListener implements Listener {

    private static final StaffHandler STAFF = StaffHandler.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        // TODO uncomment debug code
        // e.getPlayer().sendMessage(STAFF.runningCPSTests() ? "t - turn off
        // your debug when you push" : "f - turn off your debug when you push");

        if (STAFF.inStaffMode(e.getPlayer())) {
            STAFF.playerInteract(e);
        }

        if (STAFF.runningCPSTests()) {
            STAFF.handleCPSTest(e);
        }
        
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (STAFF.inStaffMode(e.getPlayer())) {
            STAFF.playerInteractEntity(e);
        }
    }

}
