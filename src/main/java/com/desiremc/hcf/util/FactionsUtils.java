package com.desiremc.hcf.util;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.FactionSession;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FactionsUtils
{

    public static Faction getFaction(String name)
    {
        Faction f = Factions.getInstance().getByTag(name);
        return f;
    }

    public static Faction getFaction(Player p)
    {
        FPlayer fp = FPlayers.getInstance().getByPlayer(p);

        return fp != null && fp.getFaction() != null ? fp.getFaction() : null;
    }

    public static Faction getFaction(Location loc)
    {
        return Board.getInstance().getFactionAt(new FLocation(loc));
    }

    public static Faction getFaction(HCFSession s)
    {
        return getFaction(s.getPlayer());
    }

    public static Faction getFaction(Session s)
    {
        return getFaction(s.getPlayer());
    }

    public static boolean isWilderness(Faction f)
    {
        return isNone(f);
    }

    public static boolean isNone(Faction f)
    {
        return f == Factions.getInstance().getNone();
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
            FactionSession fSession = FactionSessionHandler.getFactionSession(f.getTag());

            return new String[] {
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                    ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + (f != null ? f.getTag() : "NONE"),
                    ChatColor.GRAY + "Members: " + ChatColor.YELLOW + "" + (f != null ? f.getFPlayers().size() : "NONE"),
                    ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + "" + (f != null && fSession != null ? fSession.getTrophies() : "---")
            };
        }
    }

    public static String[] getMouseoverDetails(Session s)
    {
        return getMouseoverDetails(getFaction(s));
    }
    
    public static String[] getMouseoverDetails(HCFSession s)
    {
        return getMouseoverDetails(getFaction(s));
    }

    public static List<Player> getFactionMembersInRange(Player player, int range)
    {
        List<Player> inRange = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers())
        {

            if(getFaction(p) == null || getFaction(player) == null)
                continue;

            if(!getFaction(p).equals(getFaction(player)))
                continue;

            if (player.getLocation().distanceSquared(p.getLocation()) <= (range * range))
            {
                inRange.add(player);
            }
        }

        return inRange;
    }

}
