package com.desiremc.hcf.listener;

import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
            for (Region region : RegionHandler.getRegions())
            {
                if (region.getWorld() == e.getTo().getWorld() && region.getRegionBlocks().isWithin(e.getTo()))
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
            }
            if (valid && !e.isCancelled() && isOnGround(e.getPlayer()))
            {
                TagHandler.setLastValidLocation(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
            }
        }
        FSession hcfSession = FSessionHandler.getFSession(e.getPlayer().getUniqueId());
        if (hcfSession.getSafeTimeLeft() > 0)
        {
            for (Region region : RegionHandler.getRegions())
            {
                if (region.getWorld() == e.getTo().getWorld())
                {
                    if (region.getRegionBlocks().isWithin(e.getTo()))
                    {
                        hcfSession.getSafeTimer().pause();
                    }
                    else if (region.getRegionBlocks().isWithin(e.getFrom()))
                    {
                        hcfSession.getSafeTimer().resume();
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
        if (e.getTo().getWorld().getEnvironment() == Environment.THE_END && e.getFrom().getWorld().getEnvironment() != Environment.THE_END)
        {
            e.setTo(BukkitUtils.toLocation(DesireHCF.getConfigHandler().getString("set_end.spawn")));
        }
        else if (e.getTo().getWorld().getEnvironment() != Environment.THE_END && e.getFrom().getWorld().getEnvironment() == Environment.THE_END)
        {
            e.setTo(BukkitUtils.toLocation(DesireHCF.getConfigHandler().getString("set_end.exit")));
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
