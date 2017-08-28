package me.borawski.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.StaffHandler;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        StaffHandler sh = Core.getStaffHandler();

        // if (e.getItem() != null && e.getItem().getType() ==
        // Material.GOLDEN_APPLE) {
        // if (Core.getTimerHandler().hasUsedGapple(e.getPlayer())) {
        // e.setCancelled(true);
        // }
        // }

        if (sh.inStaffMode(e.getPlayer())) {
            sh.playerInteract(e);
        }

        if (sh.runningCPSTests()) {
            sh.handleCPSTest(e);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        StaffHandler sh = Core.getStaffHandler();
        if (sh.inStaffMode(e.getPlayer())) {
            sh.playerInteractEntity(e);
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
