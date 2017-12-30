package com.desiremc.hcf.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

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
            boolean valid = true;
            Region region = RegionHandler.getRegion(e.getTo());
            if (region != null)
            {
                if (TagHandler.hasLastValidLocation(e.getPlayer().getUniqueId()))
                {
                    e.setTo(TagHandler.getLastValidLocation(e.getPlayer().getUniqueId()));
                }
                else
                {
                    e.setCancelled(true);
                }
                valid = false;
            }

            if (valid && !e.isCancelled() && isOnGround(e.getPlayer()))
            {
                TagHandler.setLastValidLocation(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
        FSession fSession = FSessionHandler.getOnlineFSession(e.getPlayer().getUniqueId());
        if (fSession.getSafeTimeLeft() > 0)
        {
            for (Region region : RegionHandler.getRegions())
            {
                if (region.getWorld().equals(e.getTo().getWorld()))
                {
                    if (region.getRegionBlocks().isWithin(e.getTo()))
                    {
                        fSession.getSafeTimer().pause();
                    }
                    else
                    {
                        fSession.getSafeTimer().resume();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleportMonitor(PlayerTeleportEvent event)
    {
        if (!event.isCancelled() && TagHandler.isTagged(event.getPlayer()))
        {
            TagHandler.setLastValidLocation(event.getPlayer().getUniqueId(), event.getTo());
        }
        if (event.getTo().getWorld().getEnvironment() == Environment.THE_END && event.getFrom().getWorld().getEnvironment() != Environment.THE_END)
        {
            event.setTo(BukkitUtils.toLocation(DesireHCF.getConfigHandler().getString("endspawn")));
        }
        else if (event.getTo().getWorld().getEnvironment() != Environment.THE_END && event.getFrom().getWorld().getEnvironment() == Environment.THE_END)
        {
            event.setTo(BukkitUtils.toLocation(DesireHCF.getConfigHandler().getString("endexit")));
        }
    }

    public static boolean differentBlocks(Location l1, Location l2)
    {
        return l1.getBlockX() != l2.getBlockX() || l1.getBlockY() != l2.getBlockY() || l1.getBlockZ() != l2.getBlockZ();
    }

    private boolean isOnGround(Player player)
    {
        return !player.isFlying() && player.getLocation().subtract(0, 0.1, 0).getBlock().getType() != Material.AIR;
    }

}
