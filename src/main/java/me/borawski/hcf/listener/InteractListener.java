package me.borawski.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.StaffHandler;

public class InteractListener implements Listener {

    StaffHandler STAFF = StaffHandler.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        // if (e.getItem() != null && e.getItem().getType() ==
        // Material.GOLDEN_APPLE) {
        // if (Core.getTimerHandler().hasUsedGapple(e.getPlayer())) {
        // e.setCancelled(true);
        // }
        // }

        // TODO uncomment debug code
        //e.getPlayer().sendMessage(STAFF.runningCPSTests() ? "t - turn off your debug when you push" : "f - turn off your debug when you push");

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

    // @EventHandler
    // public void onEat(PlayerItemConsumeEvent e) {
    // if (e.getItem() != null && e.getItem().getType() ==
    // Material.GOLDEN_APPLE) {
    // if (Core.getTimerHandler().hasUsedGapple(e.getPlayer())) {
    // e.setCancelled(true);
    // } else {
    // Core.getTimerHandler().usedGapple(e.getPlayer());
    // }
    // }
    // }

}
