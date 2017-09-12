package com.desiremc.hcf.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.Utils;

public class MovementListener implements Listener
{

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if (TagHandler.isTagged(e.getPlayer()) && differentBlocks(e.getTo(), e.getPlayer().getLocation()))
        {
            for (Region r : RegionHandler.getInstance().getRegions())
            {
                if (r.getWorld().equalsIgnoreCase(e.getTo().getWorld().getName()) && r.getRegion().isWithin(e.getTo()))
                {
                    if (TagHandler.lastValidLocation.containsKey(e.getPlayer().getUniqueId()))
                    {
                        e.setTo(TagHandler.lastValidLocation.get(e.getPlayer().getUniqueId()));
                    }
                    else
                    {
                        e.setCancelled(true);
                    }
                    return;
                }
            }
            if (!e.isCancelled() && e.getPlayer().isOnGround())
            {
                TagHandler.lastValidLocation.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
        Session s = SessionHandler.getSession(e.getPlayer());
        if (s.getSafeTimeLeft() > 0)
        {
            if (!e.isCancelled() && differentBlocks(e.getTo(), e.getPlayer().getLocation()))
            {
                for (Region r : RegionHandler.getInstance().getRegions())
                {
                    if (r.getWorld().equalsIgnoreCase(e.getTo().getWorld().getName()))
                    {
                        if (r.getRegion().isWithin(e.getTo()))
                        {
                            s.getTimer().pause();
                        }
                        else if (r.getRegion().isWithin(e.getFrom()))
                        {
                            s.getTimer().resume();
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleportMonitor(PlayerTeleportEvent e)
    {
        if (!e.isCancelled() && TagHandler.isTagged(e.getPlayer()))
        {
            TagHandler.lastValidLocation.put(e.getPlayer().getUniqueId(), e.getTo());
        }
        if (e.getCause() == TeleportCause.END_PORTAL)
        {
            e.setTo(Utils.toLocation(Core.getConfigHandler().getString("set_end.spawn")));
        }
        else if (e.getCause() == TeleportCause.END_GATEWAY)
        {
            e.setTo(Utils.toLocation(Core.getConfigHandler().getString("set_end.exit")));
        }
    }

    private boolean differentBlocks(Location l1, Location l2)
    {
        return l1.getBlockX() != l2.getBlockX() || l1.getBlockY() != l2.getBlockY() || l1.getBlockZ() != l2.getBlockZ();
    }

}
