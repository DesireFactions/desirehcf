package com.desiremc.hcf.session.faction;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.events.faction.FactionDisbandEvent;
import com.desiremc.hcf.events.faction.FactionLeaveEvent;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.tasks.StuckTask;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Used to manage all the factions and base faction systems such as stuck players and admin bypass mode.
 *
 * @author Michael Ziluck
 */
public class FactionHandler extends BasicDAO<Faction, Integer>
{

    /**
     * Rate of dtr regen per minute per player online.
     */
    private static final double REGEN = 0.025;

    /**
     * Singleton instance of the handler.
     */
    private static FactionHandler instance;

    /**
     * All faction claims on the server.
     */
    private static RTree<Faction, BoundedArea> claims;

    /**
     * A map of factions referenced by the faction's name stub.
     */
    private static ConcurrentHashMap<String, Faction> factionsByName;
    /**
     * A map of factions referenced by the faction's id.
     */
    private static ConcurrentHashMap<Integer, Faction> factionsById;

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
     * The claim wand template. Should be cloned when actually used.
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
        CLAIM_WAND.setItemMeta(meta);
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

        // start up the r tree
        claims = RTree.create();

        // populate the faction map
        factionsByName = new ConcurrentHashMap<>();
        factionsById = new ConcurrentHashMap<>();
        for (Faction faction : find())
        {
            if (faction.getState() == FactionState.ACTIVE)
            {
                // add all factions to the faction map.
                factionsByName.put(faction.getStub(), faction);
                factionsById.put(faction.getId(), faction);

                for (BoundedArea claim : faction.getClaims())
                {
                    claims = claims.add(faction, claim);
                }

                if (faction.isWilderness())
                {
                    wilderness = faction;
                }
            }

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
            wilderness.setType(FactionType.WILDERNESS);
            wilderness.save();

            factionsByName.put(wilderness.getName(), wilderness);
            factionsById.put(wilderness.getId(), wilderness);
        }

        // initialize all the lists
        bypassing = new LinkedList<>();
        stuck = new HashMap<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(DesireHCF.getInstance(), () ->
        {
            for (Faction faction : factionsById.values())
            {
                if (faction.getLastDeathTime() > System.currentTimeMillis() - 1_800_000)
                {
                    continue;
                }
                if (faction.getOnlineMembers().size() == 0)
                {
                    continue;
                }
                if (faction.getDTR() >= faction.getMaxDTR())
                {
                    continue;
                }
                faction.setDTR(faction.getDTR() + ((REGEN / 120) * faction.getOnlineMembers().size()));
            }
        }, 0, 10);
    }

    /**
     * Gets the next id to be used by a faction. It will also increment the lastId so it should be called unless a new
     * faction is being created.
     *
     * @return the next faction id.
     */
    public static int getNextId()
    {
        return ++lastId;
    }

    /**
     * @return a collection of all factions.
     */
    public static Collection<Faction> getFactions()
    {
        return factionsByName.values();
    }

    /**
     * @return a collection of all factions, sorted..
     */
    public static Collection<Faction> getSortedFactions()
    {
        List<Faction> factions = new ArrayList<>();

        for (Faction f : getFactions())
        {
            if (f.getType() != FactionType.PLAYER || f.getState() != FactionState.ACTIVE)
            {
                continue;
            }
            factions.add(f);
        }

        // Sort by total members first
        Collections.sort(factions, (Faction faction1, Faction faction2) ->
        {
            int faction1Size = faction1.getMembers().size();
            int faction2Size = faction2.getMembers().size();

            if (faction1Size < faction2Size)
                return 1;
            if (faction1Size > faction2Size)
                return -1;

            return 0;
        });

        // Then sort by how many members are online now
        Collections.sort(factions, (Faction faction1, Faction faction2) ->
        {
            int faction1Size = faction1.getOnlineMembers().size();
            int faction2Size = faction2.getOnlineMembers().size();

            if (faction1Size < faction2Size)
                return 1;
            if (faction1Size > faction2Size)
                return -1;

            return 0;
        });
        return factions;
    }

    /**
     * @return a collection of all factions, sorted by trophy points
     */
    public static Collection<Faction> getSortedFactionsTrophyPoints()
    {
        List<Faction> factions = new ArrayList<>();

        for (Faction f : getFactions())
        {
            if (f.getType() != FactionType.PLAYER || f.getState() != FactionState.ACTIVE)
            {
                continue;
            }
            factions.add(f);
        }

        // Then sort by how many members are online now
        Collections.sort(factions, (Faction faction1, Faction faction2) ->
        {
            double faction1Size = faction1.getTrophyPoints();
            double faction2Size = faction2.getTrophyPoints();

            if (faction1Size < faction2Size)
                return 1;
            if (faction1Size > faction2Size)
                return -1;

            return 0;
        });
        return factions;
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

    /**
     * Get a faction that has claimed the land at the given location.
     *
     * @param location the location to search for.
     * @return the faction at the location.
     */
    public static Faction getFaction(Location location)
    {
        BlockColumn blockColumn = new BlockColumn(location.getBlockX(), location.getBlockZ(), location.getWorld());

        try
        {
            return claims.search(blockColumn).toBlocking().toFuture().get().value();
        }
        catch (InterruptedException | ExecutionException ex)
        {
            return wilderness;
        }
    }

    public static BoundedArea getArea(Location location)
    {
        BlockColumn blockColumn = new BlockColumn(location.getBlockX(), location.getBlockZ(), location.getWorld());

        try
        {
            return claims.search(blockColumn).toBlocking().toFuture().get().geometry();
        }
        catch (InterruptedException | ExecutionException ex)
        {
            return null;
        }
    }

    /**
     * Get a faction that has claimed the land at the given block column.
     *
     * @param blockColumn the block column to search for.
     * @return the faction at the block column.
     */
    public static Faction getFaction(BlockColumn blockColumn)
    {
        try
        {
            return claims.search(blockColumn).toBlocking().toFuture().get().value();
        }
        catch (InterruptedException | ExecutionException ex)
        {
            return wilderness;
        }
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
        FactionDisbandEvent disbandEvent = new FactionDisbandEvent(faction);
        Bukkit.getPluginManager().callEvent(disbandEvent);

        factionsById.remove(faction.getId());
        factionsByName.remove(faction.getStub());

        for (FSession fSession : faction.getMembers())
        {
            fSession.setFactionRank(null);
            fSession.setFaction(wilderness);
            fSession.setChannel(FactionChannel.GENERAL);
            fSession.save();
            Bukkit.getPluginManager().callEvent(new FactionLeaveEvent(faction, fSession));
        }

        faction.setState(FactionState.DELETED);
        faction.removeAllMembers();
        faction.removeAllAllies();
        faction.removeAllClaims();
        faction.save();
    }

    /**
     * Renames a faction, replaces it's values in factionsByName.
     *
     * @param faction the faction to rename.
     * @param name the new name of the faction.
     */
    public static void renameFaction(Faction faction, String name)
    {
        factionsByName.remove(faction.getStub());
        faction.setName(name);
        faction.save();
        factionsByName.put(faction.getStub(), faction);
    }

    /**
     * Creates a new {@link FactionType#PLAYER} faction. This will set the id as well as save it to the database.
     *
     * @param fSession the player creating the faction.
     * @param name the name of the faction.
     * @return the newly created faction.
     */
    public static Faction createFaction(FSession fSession, String name)
    {
        return createFaction(fSession, name, FactionType.PLAYER);
    }

    /**
     * Creates a new faction of the given type. This will set the id as well as save it to the database. Also, it will
     * add the given creator to the faction and set their {@link FactionRank} to {@link FactionRank#LEADER}.
     *
     * @param fSession the player creating the faction.
     * @param name the name of the faction.
     * @param type the type of faction.
     * @return the newly created faction.
     */
    public static Faction createFaction(FSession fSession, String name, FactionType type)
    {
        check();
        Faction faction = new Faction();
        faction.setId(getNextId());
        faction.setName(name);
        faction.setFounded(System.currentTimeMillis());
        faction.setState(FactionState.ACTIVE);
        faction.setType(type);
        faction.addMember(fSession);
        faction.setDTR(faction.getMaxDTR());
        faction.save();

        fSession.setFactionRank(FactionRank.LEADER);
        fSession.setFaction(faction);
        fSession.setChannel(FactionChannel.GENERAL);
        fSession.save();

        factionsByName.put(faction.getStub(), faction);
        factionsById.put(faction.getId(), faction);

        return faction;
    }

    // TODO potentially convert these three methods to being stored in the FSession instead.

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
    public static boolean isBypassing(FSession session)
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
    public static boolean toggleBypassing(FSession session)
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
    public static boolean isStuck(FSession session)
    {
        return stuck.containsKey(session.getUniqueId());
    }

    /**
     * Toggles stuck mode on for the given player. If they are already stuck, this method does nothing. See linked
     * method for more details of what that entails.
     *
     * @param fSession the player to toggle.
     * @return {@code true} if the player is added to the stuck.<br>
     *         {@code false} if the player is already in the stuck list.
     * @see #getStuck()
     */
    public static boolean setStuck(FSession fSession, boolean status)
    {
        if (status)
        {
            if (stuck.containsKey(fSession.getUniqueId()))
            {
                stuck.remove(fSession.getUniqueId()).cancel();
            }
            stuck.put(fSession.getUniqueId(),
                    Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(),
                            new StuckTask(fSession,
                                    fSession.getLocationColumn(),
                                    "factions.stuck.success"),
                            0L, 20L));
            return true;
        }
        else
        {
            BukkitTask task = stuck.remove(fSession.getUniqueId());
            if (task != null)
            {
                task.cancel();
            }
            return false;
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

    /**
     * Get all factions within a particular range of the given block column.
     *
     * @param blockColumn the block column.
     * @param range the range.
     * @return all nearby factions.
     */
    public static Iterable<Entry<Faction, BoundedArea>> getNearbyFactions(BlockColumn blockColumn, int range)
    {
        return claims.search(blockColumn, range).toList().toBlocking().first();
    }

    /**
     * Add a new claim to be tracked. This does NOT update the faction itself so be sure that it is added there and
     * saved to the database as well.
     *
     * @param faction the faction who has the new claim.
     * @param area the new claim area.
     */
    public static void addClaim(Faction faction, BoundedArea area)
    {
        claims = claims.add(faction, area);
    }

    /**
     * Removes a claim.
     *
     * @param faction the faction who has the claim removed.
     * @param area the removed claim area.
     */
    public static void removeClaim(Faction faction, BoundedArea area)
    {
        claims = claims.delete(faction, area);
    }

    public static void sendLogList(FSession sender, Faction faction, int page)
    {
        List<String> logs = faction.getLogs();

        if (logs.isEmpty())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.logs.none", true, false, "{faction}", faction.getName());
            return;
        }

        final int height = 9;
        int pages = (logs.size() / height) + 1;

        if (page > pages)
        {
            page = pages;
        }
        else if (page < 1)
        {
            page = 1;
        }

        int start = (page - 1) * height;
        int end = start + height;

        if (end > logs.size())
        {
            end = logs.size();
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.logs.header", false, false, "{pagenumber}", page, "{pagecount}", pages, "{faction}", faction.getName());

        int count = 1;

        for (String s : logs.subList(start, end))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.logs.log", false, false, "{log}", s, "{count}", count);
            count++;
        }
    }

    /**
     * Saves a Faction entity to the database.
     *
     * @param entity entity to save.
     * @return ??
     */
    @Override
    public Key<Faction> save(Faction entity)
    {
        return super.save(entity);
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

}
