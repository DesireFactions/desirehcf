package com.desiremc.hcf.session;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Rank;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HKitHandler extends BasicDAO<HKit, Integer>
{

    private static HKitHandler instance;

    private HashMap<Integer, HKit> kits;

    private int lastId = 0;

    /**
     * Default HKitHandler constructor. This will map the {@link HKit} database object, load all already created and
     * active HKits, and initialize the lastId value referencing the kit with the highest id.
     */
    public HKitHandler()
    {
        super(HKit.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(HKit.class);

        kits = new HashMap<>();
    }

    /**
     * Gets the next id to be used by a kit. It will also increment the lastId value by 1, so this method should not be
     * called unless a new kit is being created.
     * 
     * @return the next kit id.
     */
    public static int getNextId()
    {
        return getInstance().lastId++;
    }

    private static void check()
    {
        if (instance == null)
        {
            initialize();
        }
    }

    /**
     * Gets the raw kit map. This should not be modified as the {@link #deleteKit(int)} method does more data
     * processing.
     * 
     * @return a map of the kits.
     */
    public static Map<Integer, HKit> getKitMap()
    {
        return getInstance().kits;
    }

    /**
     * Returns a collection view of the kit map. This collection is unmodifiable and will cause errors if edited. It
     * should be used for read-only purposes.
     * 
     * @return a view of all kits.
     */
    public static Collection<HKit> getKits()
    {
        return getInstance().kits.values();
    }

    /**
     * Returns a collection view of the kit names.
     *
     * @return a view of all kit names.
     */
    public static Collection<String> getKitNames()
    {
        List<String> names = new ArrayList<>();

        for (HKit kit : getKits())
        {
            names.add(kit.getName());
        }

        return names;
    }

    /**
     * Searches for a kit by the given name. If none exists, null is returned.
     * 
     * @param name the name of the kit to search for.
     * @return the kit with the matching name.
     */
    public static HKit getKit(String name)
    {
        for (HKit kit : getInstance().kits.values())
        {
            if (kit.getName().equalsIgnoreCase(name))
            {
                return kit;
            }
        }
        return null;
    }

    /**
     * Creates a kit with the default rank of {@link Rank#GUEST}.
     * 
     * @see #createKit(String, int, Rank)
     * @param name the name of the kit.
     * @param cooldown the cooldown of the kit.
     * @return the newly created kit.
     */
    public static HKit createKit(String name, int cooldown)
    {
        return createKit(name, cooldown, Rank.GUEST);
    }

    /**
     * Creates a new kit with the given values. It will also save the record to the database so be sure to not save it
     * twice. The new kit will not have any items in it by default.
     * 
     * @param name the name of the kit.
     * @param cooldown the cooldown of the kit.
     * @param requiredRank the required rank to use the kit.
     * @return the newly created kit.
     */
    public static HKit createKit(String name, int cooldown, Rank requiredRank)
    {
        HKit kit = new HKit();
        kit.setId(getNextId());
        kit.setName(name);
        kit.setCooldown(cooldown);
        kit.setRequiredRank(requiredRank);
        kit.save();
        getInstance().kits.put(kit.getId(), kit);
        return kit;
    }

    /**
     * Initializes the instance of this handler.
     */
    public static void initialize()
    {
        instance = new HKitHandler();

        for (HKit kit : instance.createQuery().field("active").equal(true))
        {
            kit.parseContents();
            instance.kits.put(kit.getId(), kit);
            if (instance.lastId < kit.getId())
            {
                instance.lastId = kit.getId();
            }
        }
    }

    /**
     * This method will also check if the handler has been initialized, and if it hasn't it will do so.
     * 
     * @return the singleton instance of the handler.
     */
    public static HKitHandler getInstance()
    {
        check();
        return instance;
    }
}
