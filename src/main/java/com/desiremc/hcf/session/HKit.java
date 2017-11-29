package com.desiremc.hcf.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.ItemUtils;
import com.desiremc.hcf.DesireHCF;

@Entity(noClassnameStored = false, value = "hkits")
public class HKit
{

    @Id
    private int id = -1;

    private String name;

    private boolean active;

    private HashMap<Integer, String> contents;

    private Rank requiredRank;

    private int cooldown;

    @Transient
    private HashMap<Integer, ItemStack> parsedContents;

    /**
     * Will set the contents map to a new HashMap, but will not touch the parsedContents map.
     */
    public HKit()
    {
        this.contents = new HashMap<>();
    }

    /**
     * @return the unique id of the kit.
     */
    @IdGetter
    public int getId()
    {
        return id;
    }

    /**
     * Sets the id of the player. This should only be called once upon kit creation. If it is called any other time, it
     * will through an {@link IllegalStateException}.
     * 
     * @param id the new id of the kit.
     */
    protected void setId(int id)
    {
        if (id != -1)
        {
            throw new IllegalStateException("Id should never be changed once it has been set.");
        }
        this.id = id;
    }

    /**
     * @return the name of the kit.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the new name for the kit.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Whether or not the kit is deleted.
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * Sets the kit as deleted or not deleted. This allows for making sure that we do not have dead data elsewhere, and
     * allows players to historically see how often a kit was used.
     */
    public void toggleActive()
    {
        this.active = !this.active;
    }

    /**
     * This method will parse the string contents into actual ItemStack contents the first time it's called. Every call
     * after that will use the already stored version. If you want to manually force the kit to parse use
     * {@link #parseContents()}.
     * 
     * @return the item contents of the kit
     */
    public Collection<ItemStack> getContents()
    {
        check();
        return parsedContents.values();
    }

    public Map<Integer, ItemStack> getContentMap()
    {
        check();
        return parsedContents;
    }

    /**
     * This method does not need to be called ever. When an item is added or removed, it will automatically be added to
     * the parsed item list. Also, when {@link #getContents()} is called for the first time, it will automatically parse
     * the contents. This method is public to allow ease of use in the future.
     */
    public void parseContents()
    {
        parsedContents = new HashMap<>();
        for (Entry<Integer, String> entry : contents.entrySet())
        {
            parsedContents.put(entry.getKey(), ItemUtils.deserializeItem(entry.getValue()));
        }
    }

    /**
     * Shorthand method to check if the contents need to be parsed. This is just here to make it so there does not need
     * to be a ton of {@code 
     * if (parsedContents == null)
     * } calls.
     */
    private void check()
    {
        if (parsedContents == null)
        {
            parseContents();
        }
    }

    /**
     * @param item the new item to add to the kit.
     */
    public void addItem(ItemStack item)
    {
        check();
        int id = getOpenId();
        parsedContents.put(id, item);
        contents.put(id, ItemUtils.serializeItem(item));
    }

    /**
     * Remove a value from the kit by it's id value. The id value of an item can only be found by looking through
     * {@link #getContentMap()}.
     * 
     * @param id the id of the item to remove.
     */
    public void removeItem(int id)
    {
        // ensure the contents aren't null
        check();

        // remove the item from each map
        parsedContents.remove(id);
        contents.remove(id);

        // decrement the index of each other kit item
        Map<Integer, String> decrement = new HashMap<>();
        Map<Integer, ItemStack> parsedDecrement = new HashMap<>();
        Iterator<Entry<Integer, String>> it = contents.entrySet().iterator();
        Iterator<Entry<Integer, ItemStack>> parsedIt = parsedContents.entrySet().iterator();
        Entry<Integer, String> entry;
        Entry<Integer, ItemStack> parsedEntry;
        while (it.hasNext() && parsedIt.hasNext())
        {
            entry = it.next();
            parsedEntry = parsedIt.next();
            if (entry.getKey() > id && parsedEntry.getKey() > id)
            {
                decrement.put(entry.getKey() - 1, entry.getValue());
                parsedDecrement.put(parsedEntry.getKey() - 1, parsedEntry.getValue());
            }
        }

        contents.putAll(decrement);
        parsedContents.putAll(parsedDecrement);
    }

    /**
     * Retrieves the next available id to be used in the content map.
     * 
     * @return next avaialable id.
     */
    public int getOpenId()
    {
        List<Integer> ids = new ArrayList<>(parsedContents.keySet());
        Collections.sort(ids);
        for (int i = 0; i < ids.size(); i++)
        {
            if (ids.get(i) != i)
            {
                return i;
            }
        }
        return ids.size();
    }

    /**
     * @return the rank required to use this kit.
     */
    public Rank getRequiredRank()
    {
        return requiredRank;
    }

    /**
     * Sets the new required rank to use the kit.
     * 
     * @param requiredRank the new required rank.
     */
    public void setRequiredRank(Rank requiredRank)
    {
        this.requiredRank = requiredRank;
    }

    /**
     * @return the cooldown in seconds.
     */
    public int getCooldown()
    {
        return cooldown;
    }

    /**
     * Sets the new cooldown in seconds for the kit.
     * 
     * @param cooldown the cooldown for the kit.
     */
    public void setCooldown(int cooldown)
    {
        this.cooldown = cooldown;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Integer)
        {
            return Integer.valueOf(getId()).equals(obj);
        }
        else if (obj instanceof HKit)
        {
            return ((HKit) obj).getId() == getId();
        }
        else
        {
            return false;
        }
    }

    /**
     * Saves this HKit to the database asynchronously. This should not be run excessively, but it will not slow down the
     * server besides causing some network traffic.
     */
    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                HKitHandler.getInstance().save(HKit.this);
            }
        });
    }
}
