package com.desiremc.hcf.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Utils
{

    public static String chat(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String toString(Location l)
    {
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public static Location toLocation(String s)
    {
        String[] sp = s.split(",");
        return new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]) + .5, Double.parseDouble(sp[2]), Double.parseDouble(sp[3]) + .5);
    }

}
