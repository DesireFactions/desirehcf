package me.borawski.hcf.barrier;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import me.borawski.hcf.Core;

public class TagHandler {

    public static HashMap<UUID, Location> lastValidLocation = new HashMap<>();
    
    private static Cache<UUID, Long> tags;

    public static void initialize() {
        tags = CacheBuilder.newBuilder().expireAfterWrite(Core.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Long>() {

            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry) {
                if (entry.getCause().ordinal() > 2) {
                    BarrierTask.addToClear(entry.getKey());
                    Bukkit.getPlayer(entry.getKey()).sendMessage(Core.getLangHandler().getString("tag.expire"));
                }
            }
        }).build();
    }

    public static boolean isTagged(Player p) {
        return tags.asMap().containsKey(p.getUniqueId());
    }

    public static void tagPlayer(Player p, Player damager) {
        if (!isTagged(p)) {
            p.sendMessage(Core.getLangHandler().getString("tag.active"));
        }
        if (!isTagged(damager)) {
            damager.sendMessage(Core.getLangHandler().getString("tag.active"));
        }
        tags.put(p.getUniqueId(), System.currentTimeMillis());
        tags.put(damager.getUniqueId(), System.currentTimeMillis());
    }

    public static void clearTag(UUID uuid) {
        tags.invalidate(uuid);
    }

    public static Set<UUID> getTaggedPlayers() {
        return tags.asMap().keySet();
    }

}
