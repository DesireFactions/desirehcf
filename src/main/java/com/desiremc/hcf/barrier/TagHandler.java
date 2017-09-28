package com.desiremc.hcf.barrier;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.hcf.npc.SafeLogoutTask;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class TagHandler
{

    private static HashMap<UUID, Location> lastValidLocation = new HashMap<>();

    private static Cache<UUID, Long> tags;

    private static Cache<UUID, UUID> history;

    public static void initialize()
    {
        tags = CacheBuilder.newBuilder().expireAfterWrite(DesireCore.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Long>()
        {

            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                if (entry.getCause().ordinal() > 2)
                {
                    BarrierTask.addToClear(entry.getKey());
                    Bukkit.getPlayer(entry.getKey()).sendMessage(DesireCore.getLangHandler().getString("tag.expire"));
                    EntryRegistry.getInstance().removeValue(Bukkit.getPlayer(entry.getKey()),
                            DesireCore.getLangHandler().getString("tag.scoreboard"));
                }
            }
        }).build();

        history = CacheBuilder.newBuilder().expireAfterWrite(DesireCore.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS).build();
    }

    public static boolean isTagged(Player p)
    {
        return tags.asMap().containsKey(p.getUniqueId());
    }

    public static void tagPlayer(Player p, Player damager)
    {
        if (!isTagged(p))
        {
            p.sendMessage(DesireCore.getLangHandler().getString("tag.active"));
        }
        if (!isTagged(damager))
        {
            damager.sendMessage(DesireCore.getLangHandler().getString("tag.active"));
        }
        tags.put(p.getUniqueId(), System.currentTimeMillis());
        tags.put(damager.getUniqueId(), System.currentTimeMillis());
        history.put(p.getUniqueId(), damager.getUniqueId());

        SafeLogoutTask.cancel(p);
        SafeLogoutTask.cancel(damager);
    }

    public static Location getLastValidLocation(UUID uuid)
    {
        return (Location) lastValidLocation.get(uuid);
    }
  
    public static boolean hasLastValidLocation(UUID uuid)
    {
        return lastValidLocation.containsKey(uuid);
    }

    public static void setLastValidLocation(UUID uuid, Location loc)
    {
        lastValidLocation.put(uuid, loc);
    }
    
    public static Long getTagTime(UUID uuid)
    {
        return tags.getIfPresent(uuid);
    }
    
    public static UUID getTagger(UUID uuid)
    {
        return history.asMap().get(uuid);
    }

    public static void clearTag(UUID uuid)
    {
        tags.invalidate(uuid);
    }

    public static Set<UUID> getTaggedPlayers()
    {
        return tags.asMap().keySet();
    }

}
