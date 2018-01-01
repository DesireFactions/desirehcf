package com.desiremc.hcf.barrier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class BarrierTask implements Runnable
{

    private static BukkitTask task;

    private static List<UUID> toClear = new ArrayList<>();

    private HashMap<UUID, Set<Block>> cache = new HashMap<>();

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        Player p;
        for (UUID uuid : TagHandler.getTaggedPlayers())
        {
            p = PlayerUtils.getPlayer(uuid);
            if (p == null)
            {
                continue;
            }
            Set<Block> localCache = cache.get(uuid);
            if (localCache == null)
            {
                localCache = new HashSet<>();
            }

            for (Region region : RegionHandler.getRegions())
            {
                for (Block b : region.getRegionBlocks().getWallBlocks(region.getWorld()))
                {
                    if (b.getType() == Material.AIR)
                    {
                        if (b.getLocation().distanceSquared(p.getLocation()) <= region.getViewDistance() * region.getViewDistance())
                        {
                            p.sendBlockChange(b.getLocation(), region.getBarrierMaterial(), (byte) region.getBarrierMaterialData());
                            localCache.add(b);
                        }
                        else if (localCache.contains(b))
                        {
                            p.sendBlockChange(b.getLocation(), 0, (byte) 0);
                            localCache.remove(b);
                        }
                    }
                }
            }
            cache.put(uuid, localCache);
        }

        for (UUID uuid : toClear)
        {
            Player pl = PlayerUtils.getPlayer(uuid);
            if (pl != null)
            {
                Set<Block> localCache = cache.get(uuid);
                if (localCache == null)
                {
                    continue;
                }
                for (Block block : localCache)
                {
                    if (block.getType() == Material.AIR)
                    {
                        pl.sendBlockChange(block.getLocation(), 0, (byte) 0);
                    }
                }
            }
        }
        toClear.clear();

    }

    public static void addToClear(UUID uuid)
    {
        toClear.add(uuid);
    }

    public static void initialize()
    {
        if (task != null)
        {
            task.cancel();
        }
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(DesireHCF.getInstance(), new BarrierTask(),
                DesireHCF.getConfigHandler().getInteger("barrier.refresh.ticks"),
                DesireHCF.getConfigHandler().getInteger("barrier.refresh.ticks"));
    }

}
