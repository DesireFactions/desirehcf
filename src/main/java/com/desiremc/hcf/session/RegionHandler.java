package com.desiremc.hcf.session;

import java.util.Collection;
import java.util.HashMap;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.mongodb.WriteResult;

public class RegionHandler extends BasicDAO<Region, Integer>
{

    private static RegionHandler instance;

    private HashMap<String, Region> regions = new HashMap<>();

    private int nextId = 0;

    public RegionHandler()
    {
        super(Region.class, DesireCore.getInstance().getMongoWrapper().getDatastore());
        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Region.class);
        load();
    }

    public static void initialize()
    {
        instance = new RegionHandler();
    }

    private void load()
    {
        for (Region r : find().asList())
        {
            regions.put(r.getName().toLowerCase(), r);
            if (r.getId() >= nextId)
            {
                nextId = r.getId() + 1;
            }
            r.getRegion().calculate();
        }
    }

    public Region getRegion(String name)
    {
        return regions.get(name.toLowerCase());
    }

    public Collection<Region> getRegions()
    {
        return regions.values();
    }

    @Override
    public WriteResult delete(Region r)
    {
        regions.remove(r.getName().toLowerCase());
        return super.delete(r);
    }

    public Key<Region> save(Region r, boolean applyId)
    {
        r.setId(nextId);
        nextId++;
        r.getRegion().calculate();
        return this.save(r);
    }

    @Override
    public Key<Region> save(Region r)
    {
        regions.put(r.getName().toLowerCase(), r);
        r.getRegion().calculate();
        return super.save(r);
    }

    public void remove(String name)
    {
        regions.remove(name);
    }

    public static RegionHandler getInstance()
    {
        return instance;
    }

}
