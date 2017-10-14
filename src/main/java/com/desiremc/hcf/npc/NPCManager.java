package com.desiremc.hcf.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.event.NPCDespawnEvent;
import com.desiremc.hcf.event.NPCDespawnReason;

public class NPCManager
{

    private static final Map<UUID, NPC> spawnedNPCs = new HashMap<>();

    private static final Map<NPC, NPCDespawnTask> despawnTasks = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static NPC spawn(Player player)
    {
        // Do nothing if player already has a NPC
        NPC NPC = getSpawnedNPC(player.getUniqueId());
        if (NPC != null)
        {
            return null;
        }

        // Spawn fake player entity
        NPC = new NPC(NPCPlayerHelper.spawn(player));
        spawnedNPCs.put(player.getUniqueId(), NPC);

        Player entity = NPC.getEntity();

        entity.setCanPickupItems(false);
        entity.setNoDamageTicks(0);

        // Copy player data to fake player
        entity.setHealthScale(player.getHealthScale());
        entity.setMaxHealth(player.getMaxHealth());
        entity.setHealth(player.getHealth());
        entity.setTotalExperience(player.getTotalExperience());
        entity.setFoodLevel(player.getFoodLevel());
        entity.setExhaustion(player.getExhaustion());
        entity.setSaturation(player.getSaturation());
        entity.setFireTicks(player.getFireTicks());
        entity.getInventory().setContents(player.getInventory().getContents());
        entity.getInventory().setArmorContents(player.getInventory().getArmorContents());
        entity.addPotionEffects(player.getActivePotionEffects());

        // Should fix some visual glitches, such as health bars displaying zero
        entity.teleport(player, PlayerTeleportEvent.TeleportCause.PLUGIN);

        // Send equipment packets to nearby players
        NPCPlayerHelper.updateEquipment(entity);

        entity.setMetadata("NPC", new FixedMetadataValue(HCFCore.getInstance(), true));

        // Create and start the NPCs despawn task
        long despawnTime = System.currentTimeMillis() + HCFCore.getConfigHandler().getInteger("timers.npc.despawn");
        NPCDespawnTask despawnTask = new NPCDespawnTask(HCFCore.getInstance(), NPC, despawnTime);
        despawnTask.start();
        despawnTasks.put(NPC, despawnTask);

        return NPC;
    }

    public static void despawn(NPC NPC)
    {
        despawn(NPC, NPCDespawnReason.DESPAWN);
    }

    public static void despawn(NPC NPC, NPCDespawnReason reason)
    {
        // Do nothing if NPC isn't spawned or if it's a different NPC
        NPC other = getSpawnedNPC(NPC.getIdentity().getId());
        if (other == null || other != NPC)
        {
            return;
        }

        // Call NPC despawn event
        NPCDespawnEvent event = new NPCDespawnEvent(NPC, reason);
        Bukkit.getPluginManager().callEvent(event);

        // Cancel the NPCs despawn task if they have one
        if (hasDespawnTask(NPC))
        {
            NPCDespawnTask despawnTask = getDespawnTask(NPC);
            despawnTask.stop();
            despawnTasks.remove(NPC);
        }

        // Remove the NPC entity from the world
        NPCPlayerHelper.despawn(NPC.getEntity());
        spawnedNPCs.remove(NPC.getIdentity().getId());
        NPC.getEntity().removeMetadata("NPC", HCFCore.getInstance());
    }

    public static NPC getSpawnedNPC(UUID playerId)
    {
        return spawnedNPCs.get(playerId);
    }

    public static boolean NPCExists(UUID playerId)
    {
        return spawnedNPCs.containsKey(playerId);
    }

    public static NPCDespawnTask getDespawnTask(NPC NPC)
    {
        return despawnTasks.get(NPC);
    }

    public static boolean hasDespawnTask(NPC NPC)
    {
        return despawnTasks.containsKey(NPC);
    }
}
