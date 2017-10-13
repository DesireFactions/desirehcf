package com.desiremc.hcf.session;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class RegionBlocks
{
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    private double longestDistance;

    public RegionBlocks(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public RegionBlocks(Location p1, Location p2)
    {
        this.minX = Math.min(p1.getBlockX(), p2.getBlockX());
        this.minY = Math.min(p1.getBlockY(), p2.getBlockY());
        this.minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());

        this.maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        this.maxY = Math.max(p1.getBlockY(), p2.getBlockY());
        this.maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());
    }

    public RegionBlocks()
    {

    }

    public void calculate()
    {
        Vector center = getCenterPoint();
        longestDistance = distanceSquared(minX, minY, minZ, center.getX(), center.getY(), center.getZ());
    }

    public double getLongestDistance()
    {
        return longestDistance;
    }

    private double distanceSquared(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double d1 = Math.pow(x1 - x2, 2);
        double d2 = Math.pow(y1 - y2, 2);
        double d3 = Math.pow(z1 - z2, 2);

        return d1 + d2 + d3;
    }

    public int getMinX()
    {
        return minX;
    }

    public int getMinY()
    {
        return minY;
    }

    public int getMinZ()
    {
        return minZ;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public int getMaxZ()
    {
        return maxZ;
    }

    public boolean isWithin(double x, double y, double z)
    {
        return (x <= this.maxX) && (x >= this.minX) && (y <= this.maxY) && (y >= this.minY) && (z <= this.maxZ) && (z >= this.minZ);
    }

    public boolean isWithin(Location loc)
    {
        return isWithin(loc.getX(), loc.getY(), loc.getZ());
    }

    public boolean isWithin(Block b)
    {
        return isWithin(b.getLocation());
    }

    public Set<Block> getWallBlocks(World w)
    {
        HashSet<Block> walls = new HashSet<>();
        int i, j;

        for (i = this.minX; i <= this.maxX; i++)
        {
            for (j = this.minY; j <= this.maxY; j++)
            {
                walls.add(w.getBlockAt(i, j, getMinZ()));
                walls.add(w.getBlockAt(i, j, getMaxZ()));
            }
        }
        for (i = this.minZ; i <= this.maxZ; i++)
        {
            for (j = this.minY; j <= this.maxY; j++)
            {
                walls.add(w.getBlockAt(getMinX(), j, i));
                walls.add(w.getBlockAt(getMaxX(), j, i));
            }
        }
        for (i = this.minX; i <= this.maxX; i++)
        {
            for (j = this.minZ; j <= this.maxZ; j++)
            {
                walls.add(w.getBlockAt(i, getMinY(), j));
                walls.add(w.getBlockAt(i, getMaxY(), j));
            }
        }
        return walls;

    }

    public Vector getCenterPoint()
    {
        return new Vector(this.minX + (this.maxX - this.minX) / 2.0D, this.minY + (this.maxY - this.minY) / 2.0D, this.minZ + (this.maxZ - this.minZ) / 2.0D);
    }

}
