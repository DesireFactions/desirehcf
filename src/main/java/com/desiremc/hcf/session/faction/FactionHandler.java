package com.desiremc.hcf.session.faction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;

/**
 * Used to manage all the factions and base faction systems such as stuck players and admin bypass mode.
 * 
 * @author Michael Ziluck
 */
public class FactionHandler extends BasicDAO<Faction, Integer>
{

    /**
     * Singleton instance of the handler.
     */
    private static FactionHandler instance;

    /**
     * A map of factions referenced by the faction's name stub.
     */
    private static HashMap<String, Faction> factionsByName;
    /**
     * A map of factions referenced by the faction's id;
     */
    private static HashMap<Integer, Faction> factionsById;

    /**
     * The wilderness's faction instance.
     */
    private static Faction wilderness;

    /**
     * Everyone who is in bypass mode.
     */
    private static List<UUID> bypassing;

    /**
     * A map of everyone who is marked as stuck and about to be teleported out.
     */
    private static Map<UUID, BukkitTask> stuck;

    /**
     * 
     */
    private static final ItemStack CLAIM_WAND;

    /**
     * The last id used by a faction.
     */
    private static int lastId = 0;

    static
    {
        CLAIM_WAND = new ItemStack(Material.GOLD_HOE);
        ItemMeta meta = CLAIM_WAND.getItemMeta();
        meta.setDisplayName(DesireHCF.getConfigHandler().getString("factions.claim_wand.name"));
        meta.setLore(DesireHCF.getConfigHandler().getStringList("factions.claim_wand.lore"));
    }

    /**
     * Initialize all the contents of the FactionHandler. This will set the wilderness, the two faction maps, the
     * bypassing and stuck players, and the highest id.
     */
    public FactionHandler()
    {
        super(Faction.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        // map the class so it is created in the database
        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Faction.class);

        // populate the faction map
        factionsByName = new HashMap<>();
        factionsById = new HashMap<>();
        for (Faction faction : find(createQuery().field("state").equal(FactionState.ACTIVE)))
        {
            // if the faction is the wilderness, set it as such
            if (faction.getId() == -1)
            {
                wilderness = faction;
            }

            // add all factions to the faction map.
            factionsByName.put(faction.getStub(), faction);
            factionsById.put(faction.getId(), faction);

            // set the greatest id value
            if (faction.getId() > lastId)
            {
                lastId = faction.getId();
            }
        }

        // if the default faction is not in the database, we need to add it to the database
        if (wilderness == null)
        {
            wilderness = new Faction();
            wilderness.setId(-1);
            wilderness.setName("Wilderness");
            wilderness.setDescription("Land of the factionless.");
            wilderness.setState(FactionState.ACTIVE);
            wilderness.setType(FactionType.WARZONE);
            wilderness.save();
        }

        // initialize all the lists
        bypassing = new LinkedList<>();
        stuck = new HashMap<>();
    }

    /**
     * Gets the next id to be used by a faction. It will also increment the lastId so it should be called unless a new
     * faction is being created.
     * 
     * @return the next faction id.
     */
    public static int getNextId()
    {
        return lastId++;
    }

    /**
     * @return a collection of all factions.
     */
    public static Collection<Faction> getFactions()
    {
        return factionsByName.values();
    }

    /**
     * Get a faction by the given name. This method is <u>not</u> case sensitive, as names are not case sensitive as
     * well.
     * 
     * @param name the name of the faction.
     * @return the faction by the given name, if one exists.
     */
    public static Faction getFaction(String name)
    {
        return factionsByName.get(name.toLowerCase());
    }

    /**
     * Get a faction by the given id.
     * 
     * @param id the id of the faction.
     * @return the faction by the given name, if one exists.
     */
    public static Faction getFaction(int id)
    {
        return factionsById.get(id);
    }

    // TODO we should try to implement a QuadTree or R*-Tree at some point
    /**
     * Get a faction that has claimed the land at the given location.
     * 
     * @param location the location to search for.
     * @return the faction at the location.
     */
    public static Faction getFaction(Location location)
    {
        for (Faction f : getFactions())
        {
            for (BoundedArea area : f.getClaims())
            {
                if (area.contains(location))
                {
                    return f;
                }
            }
        }
        return wilderness;
    }

    /**
     * Get a faction that has claimed the land at the given block column.
     * 
     * @param blockColumn the block column to search for.
     * @return the faction at the block column.
     */
    public static Faction getFaction(BlockColumn blockColumn)
    {
        for (Faction f : getFactions())
        {
            for (BoundedArea area : f.getClaims())
            {
                if (area.contains(blockColumn))
                {
                    return f;
                }
            }
        }
        return wilderness;
    }

    /**
     * Gets the wilderness faction. This is the default faction that everyone on the server is a part of by default. The
     * reason players are not just set to a null faction is so that all of the systems that store information about
     * players in the faction still function, such as announcements.
     * 
     * @return the wilderness default faction.
     */
    public static Faction getWilderness()
    {
        return wilderness;
    }

    /**
     * Deletes a faction from the system. This method will clear the set faction for all members of the faction, unclaim
     * all land owned by this faction, and save the changed faction state from the database.
     * 
     * @param faction the faction to delete.
     */
    public static void deleteFaction(Faction faction)
    {
        check();
        for (HCFSession session : faction.getMembers())
        {
            session.setFaction(null);
            session.setFactionRank(null);
        }
        factionsById.remove(faction.getId());
        factionsByName.remove(faction.getStub());
        faction.setState(FactionState.DELETED);
        faction.save();
    }

    /**
     * Creates a new {@link FactionType#PLAYER} faction. This will set the id as well as save it to the database.
     * 
     * @param hcfSession the player creating the faction.
     * @param name the name of the faction.
     * @return the newly created faction.
     */
    public static Faction createFaction(HCFSession hcfSession, String name)
    {
        return createFaction(hcfSession, name, FactionType.PLAYER);
    }

    /**
     * Creates a new faction of the given type. This will set the id as well as save it to the database. Also, it will
     * add the given creator to the faction and set their {@link FactionRank} to {@link FactionRank#LEADER}.
     * 
     * @param hcfSession the player creating the faction.
     * @param name the name of the faction.
     * @param type the type of faction.
     * @return the newly created faction.
     */
    public static Faction createFaction(HCFSession hcfSession, String name, FactionType type)
    {
        check();
        Faction faction = new Faction();
        faction.setId(getNextId());
        faction.setName(name);
        faction.setFounded(System.currentTimeMillis());
        faction.setState(FactionState.ACTIVE);
        faction.setType(type);
        faction.save();

        hcfSession.setFactionRank(FactionRank.LEADER);
        hcfSession.setFaction(faction);

        factionsByName.put(faction.getStub(), faction);
        factionsById.put(faction.getId(), faction);

        return faction;
    }

    // TODO potentially convert these three methods to being stored in the HCFSession instead.
    /**
     * Returns a view of all the players in admin bypass mode. This view is immutable. Players in this mode are able to
     * interact with the world as if there were no faction claims, so use with great caution. Even if used accidentally,
     * overuse could lead to player upset.
     * 
     * @return a list of all players in bypass mode.
     */
    public static List<UUID> getBypassing()
    {
        return Collections.unmodifiableList(bypassing);
    }

    /**
     * Checks if this player is in admin bypass mode. See linked method for more details of what that entails.
     * 
     * @param session the player to check.
     * @return {@code true} if the player is bypassing. {@code false} otherwise.
     * @see #getBypassing()
     */
    public static boolean isBypassing(HCFSession session)
    {
        return bypassing.contains(session.getUniqueId());
    }

    /**
     * Toggles admin bypass mode on or off for the given player. See linked method for more details of what that
     * entails.
     * 
     * @param session the player to toggle.
     * @return {@code true} if the player is now bypassing.<br>
     *         {@code false} if the player is no longer bypassing.
     * @see #getBypassing()
     */
    public static boolean toggleBypassing(HCFSession session)
    {
        if (bypassing.contains(session.getUniqueId()))
        {
            bypassing.remove(session.getUniqueId());
            return false;
        }
        else
        {
            bypassing.add(session.getUniqueId());
            return true;
        }
    }

    /**
     * Returns a view of all the players who are stuck. This view is immutable. Players who are stuck are not able to
     * use /f home where they are location, and are trying to get out of some sort of situation they are not able to
     * move out of on their own accord, such as a trap.
     * 
     * @return a list of all players who are stuck.
     */
    public static Set<UUID> getStuck()
    {
        return Collections.unmodifiableSet(stuck.keySet());
    }

    /**
     * Checks if this player is stuck. See linked method for more details of what that entails.
     * 
     * @param session the player to check.
     * @return {@code true} if the player is bypassing. {@code false} otherwise.
     * @see #getStuck()
     */
    public static boolean isStuck(HCFSession session)
    {
        return stuck.containsKey(session.getUniqueId());
    }

    /**
     * Toggles stuck mode on for the given player. If they are already stuck, this method does nothing. See linked
     * method for more details of what that entails.
     * 
     * @param session the player to toggle.
     * @return {@code true} if the player is added to the stuck.<br>
     *         {@code false} if the player is already in the stuck list.
     * @see #getStuck()
     */
    public static boolean setStuck(HCFSession session)
    {
        if (stuck.containsKey(session.getUniqueId()))
        {
            return false;
        }
        else
        {
            stuck.put(session.getUniqueId(), Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new StuckTimer(), DesireHCF.getConfigHandler().getLong("factions.stuck.time")));
            return true;
        }
    }

    /**
     * @return a clone of the CLAIM_WAND constant.
     */
    public static ItemStack getClaimWand()
    {
        return CLAIM_WAND.clone();
    }

    /**
     * Checks the given item's name and lore to see if it is a claim wand.
     * 
     * @param item the item to check.
     * @return {@code true} if the passed item is a claim wand.
     */
    public static boolean isClaimWand(ItemStack item)
    {
        if (item == null || item.getType() != CLAIM_WAND.getType() || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().hasLore() || !item.getItemMeta().getDisplayName().equals(CLAIM_WAND.getItemMeta().getDisplayName()))
        {
            return false;
        }
        return true;
    }

    /**
     * Remove any and all claim wands from the given player.
     * 
     * @param player
     */
    public static void takeClaimWand(Player player)
    {
        ItemStack[] items = player.getInventory().getContents();
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] != null && FactionHandler.isClaimWand(items[i]))
            {
                player.getInventory().clear(i);
            }
        }
    }

    private static void check()
    {
        if (instance == null)
        {
            initialize();
        }
    }

    public static void initialize()
    {
        instance = new FactionHandler();

    }

    public static FactionHandler getInstance()
    {
        check();
        return instance;
    }

    /**
     * The timer used to handle stuck players.
     * 
     * @author Michael Ziluck
     * @since 12/10/2017
     */
    public static class StuckTimer implements Runnable
    {

        @Override
        public void run()
        {
            // TODO add implementation for un-sticking a player
        }

    }

    /**
     * Used to search for a particular condition of a {@link BlockColumn}.
     * 
     * @author Michael Ziluck
     */
    public static class SpiralBlockSearch implements Runnable
    {

        private Predicate<? super BlockColumn> predicate;

        private BlockColumn cursor;

        private int count = 0;

        private int length = 1;

        private SpiralDirection direction;

        /**
         * Constructs a new SpiralBlockSearch that starts at the given location and searches for the given condition.
         * 
         * @param cursor the starting point.
         * @param predicate the condition to fulfill.
         */
        public SpiralBlockSearch(BlockColumn cursor, Predicate<? super BlockColumn> predicate)
        {
            this.cursor = cursor.clone();
            this.predicate = predicate;
            this.direction = SpiralDirection.NEGATIVE_X;
        }

        @Override
        public void run()
        {
            while (!predicate.test(cursor))
            {
                if (count == length)
                {
                    count = 0;
                    length++;
                    direction = direction.nextDirection();
                }
                switch (direction)
                {
                    case NEGATIVE_X:
                        cursor.setX(cursor.getX() - 1);
                        break;
                    case POSITIVE_Z:
                        cursor.setZ(cursor.getZ() + 1);
                        break;
                    case POSITIVE_X:
                        cursor.setX(cursor.getX() + 1);
                        break;
                    case NEGATIVE_Z:
                        cursor.setZ(cursor.getZ() - 1);
                        break;
                }
                count++;
            }
        }

        /**
         * {@link #run()} must be called for the information to be changed.
         * 
         * @return the destination of the spiral search.
         */
        public BlockColumn getDestination()
        {
            return cursor;
        }

        private static enum SpiralDirection
        {
            NEGATIVE_Z,
            POSITIVE_X,
            POSITIVE_Z,
            NEGATIVE_X;

            public SpiralDirection nextDirection()
            {
                if (ordinal() == values().length - 1)
                {
                    return values()[0];
                }
                else
                {
                    return values()[ordinal() + 1];
                }
            }
        }

    }

}
