package com.desiremc.hcf.barrier;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.npc.SafeLogoutTask;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class TagHandler
{

    private static HashMap<UUID, Location> lastValidLocation = new HashMap<>();

    private static Cache<UUID, Long> tags;

    private static Cache<UUID, Tag> history;

    public static void initialize()
    {
        tags = CacheBuilder.newBuilder().expireAfterWrite(DesireHCF.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Long>()
        {

            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                if (entry.getCause().ordinal() > 2)
                {
                    BarrierTask.addToClear(entry.getKey());
                    Bukkit.getPlayer(entry.getKey()).sendMessage(DesireHCF.getLangHandler().getString("tag.expire"));
                    EntryRegistry.getInstance().removeValue(Bukkit.getPlayer(entry.getKey()), DesireHCF.getLangHandler().getString("tag.scoreboard"));
                }
            }
        }).build();

        history = CacheBuilder.newBuilder().expireAfterWrite(DesireHCF.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS).build();

        Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                SafeLogoutTask.purgeFinished();
            }
        }, 3600, 3600);
    }

    public static boolean isTagged(Player p)
    {
        return tags.asMap().containsKey(p.getUniqueId());
    }

    public static void tagPlayer(Player p, Player damager)
    {
        if (!isTagged(p))
        {
            p.sendMessage(DesireHCF.getLangHandler().getString("tag.active"));
        }
        if (!isTagged(damager))
        {
            damager.sendMessage(DesireHCF.getLangHandler().getString("tag.active"));
        }
        tags.put(p.getUniqueId(), System.currentTimeMillis());
        tags.put(damager.getUniqueId(), System.currentTimeMillis());
        history.put(p.getUniqueId(), new Tag(damager.getUniqueId(), damager.getInventory().getItemInMainHand()));

        SafeLogoutTask.cancel(p);
        SafeLogoutTask.cancel(damager);
    }

    public static Location getLastValidLocation(UUID uuid)
    {
        return lastValidLocation.get(uuid);
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
        Tag tag = history.getIfPresent(uuid);
        if (tag == null)
        {
            return null;
        }
        return tag.getUniqueId();
    }

    public static Tag getTag(UUID uuid)
    {
        return history.getIfPresent(uuid);
    }

    public static void clearTag(UUID uuid)
    {
        tags.invalidate(uuid);
    }

    public static Set<UUID> getTaggedPlayers()
    {
        return tags.asMap().keySet();
    }

    public static class Tag
    {
        private UUID uuid;
        private ItemStack item;

        public Tag(UUID uuid, ItemStack item)
        {
            this.uuid = uuid;
            this.item = item;
        }

        public UUID getUniqueId()
        {
            return uuid;
        }

        public ItemStack getItem()
        {
            return item;
        }
    }

}
