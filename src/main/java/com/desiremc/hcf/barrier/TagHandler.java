package com.desiremc.hcf.barrier;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class TagHandler
{

    public static HashMap<UUID, Location> lastValidLocation = new HashMap<>();

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
