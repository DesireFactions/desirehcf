package com.desiremc.hcf.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;

public class RegionHandler extends BasicDAO<Region, Integer>
{

    private static RegionHandler instance;

    private static HashMap<String, Region> regions = new HashMap<>();

    private static int nextId = 0;

    public RegionHandler()
    {
        super(Region.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Region.class);

        loadRegions();
    }

    public static void initialize()
    {
        instance = new RegionHandler();
    }

    private void loadRegions()
    {
        for (Region region : find())
        {
            regions.put(region.getName().toLowerCase(), region);
            if (region.getId() >= nextId)
            {
                nextId = region.getId() + 1;
            }
        }
    }

    /**
     * Get a region by the given case-insensitive name if one exists.
     * 
     * @param name the name of the region to search for.
     * @return the region if one is found.
     */
    public static Region getRegion(String name)
    {
        return regions.get(name.toLowerCase());
    }

    /**
     * @return an immutable view of all the regions.
     */
    public static Collection<Region> getRegions()
    {
        return regions.values();
    }

    /**
     * @return a mutable list of all region names.
     */
    public static List<String> getRegionNames()
    {
        List<String> names = new LinkedList<>();
        for (Region region : getRegions())
        {
            names.add(region.getName());
        }
        return names;
    }

    /**
     * Delete a region from the database.
     * 
     * @param region the region to delete.
     */
    public static void deleteRegion(Region region)
    {
        regions.remove(region.getName().toLowerCase());
        getInstance().delete(region);
    }

    /**
     * Create a new region with the given values. This will also save the region to the database.
     * 
     * @param name the name of the region.
     * @param world the world the region exists in.
     * @param regionBlocks the blocks of the region.
     * @param barrierMaterial the material the barrier is made up of.
     * @param viewDistance the distance a player can view the barrier from.
     * @return the newly created region.
     */
    public static Region createRegion(String name, World world, RegionBlocks regionBlocks, MaterialData barrierMaterial, int viewDistance)
    {
        Region region = new Region();
        region.setId(nextId);
        region.setName(name);
        region.setWorld(world);
        region.setRegionBlocks(regionBlocks);
        region.setBarrierMaterial(barrierMaterial);
        region.setViewDistance(viewDistance);
        region.save();

        nextId++;

        return region;
    }

    /**
     * @return the singleton instance of this handler.
     */
    public static RegionHandler getInstance()
    {
        return instance;
    }

}
