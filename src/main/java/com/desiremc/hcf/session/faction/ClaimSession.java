package com.desiremc.hcf.session.faction;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.desiremc.core.utils.BlockColumn;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;

public class ClaimSession implements Runnable
{

    private BlockColumn pointOne;

    private BlockColumn pointTwo;

    private boolean deleted;

    private FSession hcfSession;

    /**
     * @param uuid
     * @param pointOne
     * @param pointTwo
     */
    public ClaimSession(FSession hcfSession)
    {
        this.hcfSession = hcfSession;
    }

    /**
     * @return the hcfSession
     */
    public FSession getHcfSession()
    {
        return hcfSession;
    }

    /**
     * @param hcfSession the hcfSession to set
     */
    public void setHcfSession(FSession hcfSession)
    {
        this.hcfSession = hcfSession;
    }

    /**
     * @return the pointOne
     */
    public BlockColumn getPointOne()
    {
        return pointOne;
    }

    /**
     * @param pointOne the pointOne to set
     */
    public void setPointOne(BlockColumn pointOne)
    {
        this.pointOne = pointOne;
    }

    /**
     * @return {@code true} if the first point is set.
     */
    public boolean hasPointOne()
    {
        return pointOne != null;
    }

    /**
     * @return the pointTwo
     */
    public BlockColumn getPointTwo()
    {
        return pointTwo;
    }

    /**
     * @param pointTwo the pointTwo to set
     */
    public void setPointTwo(BlockColumn pointTwo)
    {
        this.pointTwo = pointTwo;
    }

    /**
     * @return {@code true} if the second point is set.
     */
    public boolean hasPointTwo()
    {
        return pointTwo != null;
    }

    /**
     * @return {@code true} if the claim session has been terminated.
     */
    public boolean isDeleted()
    {
        return deleted;
    }

    /**
     * Sets this claim session as deleted.
     */
    public void setDeleted()
    {
        this.deleted = true;
    }

    /**
     * Gets the current area of the claim session. If the
     * 
     * @return the current area.
     */
    public int getArea()
    {
        return (int) new BoundedArea(pointOne, pointTwo).area();
    }

    /**
     * @return the cost to claim the current selection. -1 if both points aren't set.
     */
    public double getCost()
    {
        if (pointOne == null || pointTwo == null)
        {
            return -1;
        }
        double claimScale = hcfSession.getFaction().getClaims().size() * DesireHCF.getConfigHandler().getDouble("factions.claims.cost.existing_scale");
        double blockScale = (Math.max(pointOne.getX(), pointTwo.getX()) - Math.min(pointOne.getX(), pointTwo.getX())) // get the width
                * (Math.max(pointOne.getX(), pointTwo.getX()) - Math.min(pointOne.getX(), pointTwo.getX())) // get the length
                * (DesireHCF.getConfigHandler().getDouble("factions.claims.cost.block")); // get the scale per block

        return claimScale + blockScale;
    }

    /**
     * @return the x difference.
     */
    public int getLength()
    {
        return Math.max(pointOne.getX(), pointTwo.getX()) - Math.min(pointOne.getX(), pointTwo.getX());
    }

    /**
     * @return the y difference.
     */
    public int getWidth()
    {
        return Math.max(pointOne.getZ(), pointTwo.getZ()) - Math.min(pointOne.getZ(), pointTwo.getZ());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        // if the session is marked as deleted, clear all the block changes and terminate the runnable loop.
        if (deleted)
        {
            List<Block> blocks = new LinkedList<>();
            if (pointOne != null)
            {
                blocks.addAll(pointOne.getAllBlocks());
            }
            if (pointTwo != null)
            {
                blocks.addAll(pointTwo.getAllBlocks());
            }
            for (Block block : blocks)
            {
                if (block.getType() == Material.AIR)
                {
                    hcfSession.getPlayer().sendBlockChange(block.getLocation(), Material.AIR, (byte) 0);
                }
            }
            return;
        }

        // set the markers if the points are set
        if (pointOne != null)
        {
            List<Block> blocks = pointOne.getAllBlocks();
            Block block;
            for (int i = 0; i < blocks.size(); i++)
            {
                block = blocks.get(i);
                if (block != null && block.getType() == Material.AIR)
                {
                    hcfSession.getPlayer().sendBlockChange(block.getLocation(), i % 3 == 0 ? Material.DIAMOND_BLOCK : Material.GLASS, (byte) 0);
                }
            }
        }
        if (pointTwo != null)
        {
            List<Block> blocks = pointTwo.getAllBlocks();
            Block block;
            for (int i = 0; i < 257; i++)
            {
                block = blocks.get(i);
                if (block != null && block.getType() == Material.AIR)
                {
                    hcfSession.getPlayer().sendBlockChange(block.getLocation(), i % 3 == 0 ? Material.DIAMOND_BLOCK : Material.GLASS, (byte) 0);
                }
            }
        }

        // loop this task in 10 ticks
        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), this, 10l);
    }

}
