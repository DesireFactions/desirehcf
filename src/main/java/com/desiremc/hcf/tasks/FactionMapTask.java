package com.desiremc.hcf.tasks;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.desiremc.core.utils.BlockColumn;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.github.davidmoten.rtree.Entry;

public class FactionMapTask implements Runnable
{

    private FSession fSession;

    private List<BlockColumn> current = new LinkedList<>();

    private boolean cancelled;

    public FactionMapTask(FSession fSession)
    {
        this.fSession = fSession;
    }

    public void cancel()
    {
        this.cancelled = true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        // if it's cancelled
        if (cancelled)
        {
            // if it's null, somehow this got ran again, exit.
            if (current == null)
            {
                return;
            }
            // send the player the actual blocks
            wipeCurrent();
            // set current to null
            current = null;
            // exit
            return;
        }
        
        // what is going to be sent to the player
        List<BlockColumn> sending = new LinkedList<>();
        // where the player is
        BlockColumn location = fSession.getLocationColumn();
        // loop through nearby claims
        for (Entry<Faction, BoundedArea> entry : FactionHandler.getNearbyFactions(location, 50))
        {
            // add the corners to sending
            for (BlockColumn column : entry.geometry().corners())
            {
                sending.add(column);
            }
        }

        // go through what we're sending
        for (BlockColumn column : sending)
        {
            // if we havent sent it, or they are close enough to interact
            if (!current.contains(column) || location.distance(column) < 6)
            {
                // get the blocks
                List<Block> blocks = column.getAllBlocks();
                // used as a cursor
                Block block;
                // loop through the column, send them the block change
                for (int i = 0; i < blocks.size(); i++)
                {
                    block = blocks.get(i);
                    if (block != null && block.getType() == Material.AIR)
                    {
                        fSession.getPlayer().sendBlockChange(block.getLocation(), i % 3 == 0 ? Material.GOLD_BLOCK : Material.GLASS, (byte) 0);
                    }
                }
            }
        }

        // remove anything not close by
        current.removeAll(sending);

        // send the player the actual blocks
        wipeCurrent();

        // set the current to what was just sent
        current = sending;
        
        // loop the event
        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), this, 20L);
    }

    @SuppressWarnings("deprecation")
    private void wipeCurrent()
    {
        for (BlockColumn column : current)
        {
            for (Block block : column.getAllBlocks())
            {
                if (block.getType() == Material.AIR)
                {
                    if (fSession.isOnline())
                    {
                        fSession.getPlayer().sendBlockChange(block.getLocation(), Material.AIR, (byte) 0);
                    }
                }
            }
        }
    }

}
