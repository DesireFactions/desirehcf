package com.desiremc.hcf.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.session.Session;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionType;

public class FactionsUtils
{

    /**
     * Gets the {@link Faction} by the given id, if one exists.
     * 
     * @param id the id to check.
     * @return the {@link Faction} if found.
     */
    public static Faction getFaction(int id)
    {
        Faction faction = FactionHandler.getFaction(id);
        return faction;
    }

    /**
     * Get the {@link Faction} by the given name, if one exists.
     * 
     * @param name the name to check.
     * @return the {@link Faction} if found.
     */
    public static Faction getFaction(String name)
    {
        return FactionHandler.getFaction(name);
    }

    /**
     * Get the faction that has claimed the land at the given location.
     * 
     * @param loc the location to check.
     * @return the faction if one exists.
     */
    public static Faction getFaction(Location loc)
    {
        return FactionHandler.getFaction(loc);
    }

    /**
     * Get the faction that has claimed the land at the given block column.
     * 
     * @param blockColumn the block column.
     * @return the faction if one exists.
     */
    public static Faction getFaction(BlockColumn blockColumn)
    {
        return FactionHandler.getFaction(blockColumn);
    }

    /**
     * Get the faction of the given {@link Player}.
     * 
     * @param player the {@link Player} to check.
     * @return the {@link Faction} if the {@link Player} has one.
     */
    public static Faction getFaction(Player player)
    {
        return getFaction(player.getUniqueId());
    }

    /**
     * Get the faction of the given {@link Session}.
     * 
     * @param session the {@link Session} to check.
     * @return the {@link Faction} if the {@link Session} has one.
     */
    public static Faction getFaction(Session session)
    {
        return getFaction(session.getUniqueId());
    }

    /**
     * Get the faction of the player referenced by their UUID. If the player is not found, returns null rather than the
     * Wilderness faction.
     * 
     * @param uuid the uuid of the player to search for.
     * @return
     */
    public static Faction getFaction(UUID uuid)
    {
        FSession fSession = FSessionHandler.getGeneralFSession(uuid);

        return fSession != null && fSession.getFaction() != null ? fSession.getFaction() : null;
    }

    /**
     * Get the faction of the given {@link FSession}.
     * 
     * @param fSession the {@link FSession} to check.
     * @return the {@link Faction} if the {@link FSession} has one.
     */
    public static Faction getFaction(FSession fSession)
    {
        return fSession.getFaction();
    }

    /**
     * @return an immutable view of all the factions.
     */
    public static Collection<Faction> getFactions()
    {
        return Collections.unmodifiableCollection(FactionHandler.getFactions());
    }

    /**
     * @return a mutable collection of all factions' names.
     */
    public static Collection<String> getFactionNames()
    {
        List<String> names = new LinkedList<>();
        for (Faction faction : FactionHandler.getFactions())
        {
            names.add(faction.getName());
        }
        return names;
    }

    /**
     * Checks if the given {@link Faction} is the Wilderness.
     * 
     * @param f the faction to check.
     * @return {@code true} if the given faction is the Wilderness.
     */
    public static boolean isWilderness(Faction f)
    {
        return isNone(f);
    }

    /**
     * Checks if the given {@link Faction} is nothing. In the current Faction system, this is the same as checking if
     * the faction {@link #isWilderness(Faction)}.
     * 
     * @param f the faction to check.
     * @return {@code true} if the given faction is the Wilderness.
     */
    public static boolean isNone(Faction f)
    {
        if (f == null)
        {
            return false;
        }
        return f.isWilderness();
    }

    public static String[] getMouseoverDetails(Faction f)
    {

        if (isNone(f))
        {
            return new String[] {
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "NO FACTION"
            };
        }
        else
        {
            return new String[] {
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                    ChatColor.GRAY + "Name: " + ChatColor.YELLOW + f.getName(),
                    ChatColor.GRAY + "Members: " + ChatColor.YELLOW + f.getMemberSize(),
                    ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + f.getTrophyPoints()
            };
        }
    }

    public static String[] getMouseoverDetails(Session s)
    {
        return getMouseoverDetails(getFaction(s));
    }

    public static String[] getMouseoverDetails(FSession s)
    {
        return getMouseoverDetails(getFaction(s));
    }

    /**
     * Get a list of faction members that are within the given range of the given player.
     * 
     * @param player the player
     * @param range the range
     * @return a list of faction members within range.
     */
    public static List<Player> getFactionMembersInRange(Player player, int range)
    {
        List<Player> inRange = new ArrayList<>();

        Faction faction = getFaction(player);
        if (faction == null || faction.isWilderness())
        {
            return inRange;
        }

        for (FSession member : faction.getOnlineMembers())
        {
            if (member.getPlayer() != player)
            {
                if (player.getLocation().distanceSquared(member.getPlayer().getLocation()) <= (range * range))
                {
                    inRange.add(member.getPlayer());
                }
            }
        }

        for (Player p : inRange)
        {
            System.out.println(p.getName());
        }
        System.out.println(faction.getName());

        return inRange;
    }

    /**
     * Get a list of all non-allies that are within the given range of the given player.
     * 
     * @param player the player
     * @param range the range
     * @return a list of non-allies within range.
     */
    public static List<Player> getEnemiesInRange(Player player, int range)
    {
        List<Player> inRange = new ArrayList<>();

        Faction faction = getFaction(player);
        if (faction == null || faction.isWilderness())
        {
            return inRange;
        }

        for (FSession session : FSessionHandler.getOnlineFSessions())
        {
            if (session.getFaction().getRelationshipTo(faction).canAttack())
            {
                inRange.add(session.getPlayer());
            }
        }

        return inRange;
    }

    /**
     * Gets a list of all allies and faction members within the given range of the given player.
     * 
     * @param player the player
     * @param range the range
     * @return a list of allies and faction members.
     */
    public static List<Player> getAlliesInRange(Player player, int range)
    {
        List<Player> inRange = new ArrayList<>();

        Faction faction = getFaction(player);
        if (faction == null || faction.isWilderness())
        {
            return inRange;
        }

        for (Faction ally : faction.getAllies())
        {
            for (FSession fSession : ally.getOnlineMembers())
            {
                if (fSession.getPlayer().getLocation().distanceSquared(player.getLocation()) <= (range * range))
                {
                    inRange.add(fSession.getPlayer());
                }
            }
        }

        for (FSession member : faction.getOnlineMembers())
        {
            if (member.getPlayer() != player && member.getPlayer().getLocation().distanceSquared(player.getLocation()) <= (range * range))
            {
                inRange.add(member.getPlayer());
            }
        }

        return inRange;
    }

    public static Collection<FSession> getOnlineFSessions()
    {
        LinkedList<FSession> online = new LinkedList<>(FSessionHandler.getFSessions());

        online.removeIf(check -> !check.isOnline());

        return online;
    }

    public static boolean isInSafeZone(Player player)
    {
        Faction faction = getFaction(player.getLocation());

        return faction.getType() == FactionType.SAFEZONE;
    }

}
