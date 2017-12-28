package com.desiremc.hcf.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.hcf.DesireHCF;

@Entity(value = "regions", noClassnameStored = true)
@Indexes(@Index(fields = @Field(value = "name"), options = @IndexOptions(unique = true)))
public class Region
{

    @Id
    private int id;

    private String name;

    @Transient
    private World parsedWorld;
    private String world;

    private int viewDistance;

    @Embedded
    private RegionBlocks regionBlocks;

    private Material barrierMaterial;
    private short barrierMaterialData;

    @IdGetter
    /**
     * @return the id of the region.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Sets the id of the region. For data assurance, this should never be used unless the region is being created for
     * the first time.
     * 
     * @param id the new id.
     */
    protected void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the name of the region.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the new name of the region.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the world for this region
     */
    public World getWorld()
    {
        if (parsedWorld == null)
        {
            parsedWorld = Bukkit.getWorld(world);
        }
        return parsedWorld;
    }

    /**
     * @param world the new world for the region.
     */
    public void setWorld(World world)
    {
        this.parsedWorld = world;
        this.world = world.getName();
    }

    /**
     * @return how far away a player can see the region.
     */
    public int getViewDistance()
    {
        return viewDistance;
    }

    /**
     * @param viewDistance the new view distance of the region.
     */
    public void setViewDistance(int viewDistance)
    {
        this.viewDistance = viewDistance;
    }

    /**
     * @return the region blocks for the region.
     */
    public RegionBlocks getRegionBlocks()
    {
        return regionBlocks;
    }

    /**
     * @param regionBlocks the new region blocks.
     */
    public void setRegionBlocks(RegionBlocks regionBlocks)
    {
        this.regionBlocks = regionBlocks;
    }

    public Material getBarrierMaterial()
    {
        return barrierMaterial;
    }

    public short getBarrierMaterialData()
    {
        return barrierMaterialData;
    }

    @SuppressWarnings("deprecation")
    public void setBarrierMaterial(MaterialData data)
    {
        this.barrierMaterial = data.getItemType();
        this.barrierMaterialData = data.getData();
        save();
    }

    /**
     * Save the region asynchronously.
     */
    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                RegionHandler.getInstance().save(Region.this);
            }
        });
    }
}
