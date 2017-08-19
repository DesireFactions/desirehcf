package me.borawski.hcf.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.borawski.hcf.barrier.TagHandler;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionHandler;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class MovementListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (TagHandler.isTagged(e.getPlayer()) && differentBlocks(e.getTo(), e.getPlayer().getLocation())) {
            for (Region r : RegionHandler.getInstance().getRegions()) {
                if (r.getWorld().equalsIgnoreCase(e.getTo().getWorld().getName()) && r.getRegion().isWithin(e.getTo())) {
                    if (TagHandler.lastValidLocation.containsKey(e.getPlayer().getUniqueId())) {
                        e.setTo(TagHandler.lastValidLocation.get(e.getPlayer().getUniqueId()));
                    } else {
                        e.setCancelled(true);
                    }
                    return;
                }
            }
            if (!e.isCancelled() && e.getPlayer().isOnGround()) {
                TagHandler.lastValidLocation.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
        Session s = SessionHandler.getSession(e.getPlayer());
        if (s.getSafeTimeLeft() > 0) {
            if (!e.isCancelled() && differentBlocks(e.getTo(), e.getPlayer().getLocation())) {
                for (Region r : RegionHandler.getInstance().getRegions()) {
                    if (r.getWorld().equalsIgnoreCase(e.getTo().getWorld().getName())) {
                        if (r.getRegion().isWithin(e.getTo())) {
                            s.getTimer().pause();
                        } else if (r.getRegion().isWithin(e.getFrom())) {
                            s.getTimer().resume();
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleportMonitor(PlayerTeleportEvent e) {
        if (!e.isCancelled() && TagHandler.isTagged(e.getPlayer())) {
            TagHandler.lastValidLocation.put(e.getPlayer().getUniqueId(), e.getTo());
        }
    }

    private boolean differentBlocks(Location l1, Location l2) {
        return l1.getBlockX() != l2.getBlockX() || l1.getBlockY() != l2.getBlockY() || l1.getBlockZ() != l2.getBlockZ();
    }

}
