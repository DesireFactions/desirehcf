package com.desiremc.hcf.handler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;

public class EndHandler implements Listener
{

    private static EndHandler instance;
    private Location endExit;
    private Location endSpawn;

    public EndHandler()
    {
        instance = this;
    }

    public static EndHandler getInstance()
    {
        return instance;
    }

    @EventHandler
    public void onTeleport(PlayerPortalEvent event)
    {

        if (!event.isCancelled() && TagHandler.isTagged(event.getPlayer()))
        {
            TagHandler.setLastValidLocation(event.getPlayer().getUniqueId(), event.getTo());
        }
        if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END && event.getFrom().getWorld().getEnvironment() != World.Environment.THE_END)
        {
            event.setTo(getEndSpawn());
        }
        else if (event.getTo().getWorld().getEnvironment() != World.Environment.THE_END && event.getFrom().getWorld().getEnvironment() == World.Environment.THE_END)
        {
            event.setTo(getEndExit());
        }
        else if (event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL && event.getFrom().getWorld().getEnvironment() == World.Environment.NETHER)
        {
            event.setTo(SpawnHandler.getInstance().getSpawn());
        }
    }

    public Location getEndExit()
    {
        if (endExit == null)
        {
            endExit = BukkitUtils.toLocation(DesireHCF.getConfigHandler().getString("endexit"));
        }
        return endExit;
    }

    public Location getEndSpawn()
    {
        if (endSpawn == null)
        {
            endSpawn = BukkitUtils.toLocation(DesireHCF.getConfigHandler().getString("endspawn"));
        }
        return endSpawn;
    }
}
