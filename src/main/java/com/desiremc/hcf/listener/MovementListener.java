package com.desiremc.hcf.listener;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.utils.Utils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MovementListener implements Listener
{

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if (!differentBlocks(e.getFrom(), e.getTo()) || e.isCancelled())
        {
            return;
        }
        if (TagHandler.isTagged(e.getPlayer()))
        {
            for (Region r : RegionHandler.getInstance().getRegions())
            {
                if (r.getWorld().equalsIgnoreCase(e.getTo().getWorld().getName()) && r.getRegion().isWithin(e.getTo()))
                {
                    if (TagHandler.hasLastValidLocation(e.getPlayer().getUniqueId()))
                    {
                        e.setTo(TagHandler.getLastValidLocation(e.getPlayer().getUniqueId()));
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
                TagHandler.setLastValidLocation(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
        HCFSession s = HCFSessionHandler.getHCFSession(e.getPlayer().getUniqueId());
        if (s.getSafeTimeLeft() > 0)
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleportMonitor(PlayerTeleportEvent e)
    {
        if (!e.isCancelled() && TagHandler.isTagged(e.getPlayer()))
        {
            TagHandler.setLastValidLocation(e.getPlayer().getUniqueId(), e.getTo());
        }
        if (e.getCause() == TeleportCause.END_PORTAL)
        {
            e.setTo(Utils.toLocation(DesireHCF.getConfigHandler().getString("set_end.spawn")));
        }
        else if (e.getCause() == TeleportCause.END_GATEWAY)
        {
            e.setTo(Utils.toLocation(DesireHCF.getConfigHandler().getString("set_end.exit")));
        }
    }

    public static boolean differentBlocks(Location l1, Location l2)
    {
        return l1.getBlockX() != l2.getBlockX() || l1.getBlockY() != l2.getBlockY() || l1.getBlockZ() != l2.getBlockZ();
    }

}
