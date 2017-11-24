package com.desiremc.hcf.util;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

import static com.desiremc.hcf.DesireHCF.getWorldGuard;
import static com.sk89q.worldedit.bukkit.BukkitUtil.toVector;

public class WorldGuardUtils
{

    public static boolean isPlayerInRegion(Location loc, String region)
    {
        WorldGuardPlugin guard = getWorldGuard();
        Vector v = toVector(loc);
        RegionManager manager = guard.getRegionManager(loc.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(v);
        for (ProtectedRegion each : set)
        {
            if (each.getId().equalsIgnoreCase(region))
            {
                return true;
            }
        }
        return false;
    }
}
