package com.desiremc.hcf.barrier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.core.utils.cache.RemovalNotification.Cause;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.tasks.SafeLogoutTask;

public class TagHandler
{

    private static HashMap<UUID, Location> lastValidLocation = new HashMap<>();

    private static Cache<String, Long> tags;

    private static Cache<UUID, Tag> history;

    public static void initialize()
    {
        tags = new Cache<>(DesireHCF.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS, new RemovalListener<String, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<String, Long> entry)
            {
                if (entry.getCause() == Cause.EXPIRE || entry.getCause() == Cause.REMOVE)
                {
                    UUID uuid = UUID.fromString(entry.getKey());
                    Player p = PlayerUtils.getPlayer(uuid);
                    if (p != null)
                    {
                        BarrierTask.addToClear(uuid);
                        DesireHCF.getLangHandler().sendRenderMessage(p, "tag.expire", true, false);
                        EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().renderMessage("tag.scoreboard", false, false));
                    }
                }
            }
        }, DesireHCF.getInstance());

        history = new Cache<>(DesireHCF.getConfigHandler().getInteger("tag.time"), TimeUnit.SECONDS, DesireHCF.getInstance());

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
        return tags.containsKey(p.getUniqueId().toString());
    }

    public static void tagPlayer(Player p, Player damager)
    {
        if (!isTagged(p))
        {
            p.sendMessage(DesireHCF.getLangHandler().getString("tag.active"));
        }
        if (p != damager && !isTagged(damager))
        {
            damager.sendMessage(DesireHCF.getLangHandler().getString("tag.active"));
        }
        tags.put(p.getUniqueId().toString(), System.currentTimeMillis());
        tags.put(damager.getUniqueId().toString(), System.currentTimeMillis());
        history.put(p.getUniqueId(), new Tag(damager.getUniqueId(), damager.getItemInHand()));

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
        return tags.get(uuid.toString());
    }

    public static UUID getTagger(UUID uuid)
    {
        Tag tag = history.get(uuid);
        if (tag == null)
        {
            return null;
        }
        return tag.getUniqueId();
    }

    public static Tag getTag(UUID uuid)
    {
        return history.get(uuid);
    }

    public static void clearTag(UUID uuid)
    {
        tags.remove(uuid);
    }

    public static Set<UUID> getTaggedPlayers()
    {
        Set<UUID> set = new HashSet<>();
        for (String s : tags.keySet())
        {
            set.add(UUID.fromString(s));
        }
        return set;
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
