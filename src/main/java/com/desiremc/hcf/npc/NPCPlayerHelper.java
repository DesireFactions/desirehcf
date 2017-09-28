package com.desiremc.hcf.npc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.FoodMetaData;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.WorldNBTStorage;
import net.minecraft.server.v1_12_R1.WorldServer;

public class NPCPlayerHelper
{

    public static Player spawn(Player player)
    {
        NPCPlayer npcPlayer = NPCPlayer.valueOf(player);
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
        Location l = player.getLocation();
        MinecraftServer server = getServer();

        npcPlayer.spawnIn(worldServer);
        npcPlayer.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        npcPlayer.playerInteractManager.a(worldServer);
        npcPlayer.invulnerableTicks = 0;

        for (Object o : server.getPlayerList().players)
        {
            if (!(o instanceof EntityPlayer) || o instanceof NPCPlayer) continue;

            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npcPlayer);
            ((EntityPlayer) o).playerConnection.sendPacket(packet);
        }

        worldServer.addEntity(npcPlayer);
        worldServer.getPlayerChunkMap().addPlayer(npcPlayer);

        return npcPlayer.getBukkitEntity();
    }

    public static void despawn(Player player)
    {
        EntityPlayer entity = ((CraftPlayer) player).getHandle();

        if (!(entity instanceof NPCPlayer)) { throw new IllegalArgumentException(); }

        for (Object o : getServer().getPlayerList().players)
        {
            if (!(o instanceof EntityPlayer) || o instanceof NPCPlayer) continue;

            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entity);
            ((EntityPlayer) o).playerConnection.sendPacket(packet);
        }

        WorldServer worldServer = getServer().getWorldServer(entity.dimension);
        worldServer.removeEntity(entity);
        worldServer.getPlayerChunkMap().removePlayer(entity);
    }

    public static boolean isNpc(Player player)
    {
        return ((CraftPlayer) player).getHandle() instanceof NPCPlayer;
    }

    public static NPCIdentity getIdentity(Player player)
    {
        if (!isNpc(player)) { throw new IllegalArgumentException(); }

        return ((NPCPlayer) ((CraftPlayer) player).getHandle()).getNpcIdentity();
    }

    public static void updateEquipment(Player player)
    {
        EntityPlayer entity = ((CraftPlayer) player).getHandle();

        if (!(entity instanceof NPCPlayer)) { throw new IllegalArgumentException(); }

        Location l = player.getLocation();
        int rangeSquared = 512 * 512;

        for (EnumItemSlot slot : EnumItemSlot.values())
        {
            ItemStack item = entity.getEquipment(slot);
            if (item == null) continue;

            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entity.getId(), slot, item);

            for (Object o : entity.world.players)
            {
                if (!(o instanceof EntityPlayer)) continue;

                EntityPlayer p = (EntityPlayer) o;
                Location loc = p.getBukkitEntity().getLocation();
                if (l.getWorld().equals(loc.getWorld()) && l.distanceSquared(loc) <= rangeSquared)
                {
                    p.playerConnection.sendPacket(packet);
                }
            }
        }
    }

    public static void syncOffline(Player player)
    {
        EntityPlayer entity = ((CraftPlayer) player).getHandle();

        if (!(entity instanceof NPCPlayer)) { throw new IllegalArgumentException(); }

        NPCPlayer npcPlayer = (NPCPlayer) entity;
        NPCIdentity identity = npcPlayer.getNpcIdentity();
        Player p = Bukkit.getPlayer(identity.getId());
        if (p != null && p.isOnline()) return;

        WorldNBTStorage worldStorage = (WorldNBTStorage) ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle().getDataManager();
        NBTTagCompound playerNbt = worldStorage.getPlayerData(identity.getId().toString());
        if (playerNbt == null) return;

        // foodTickTimer is now private in 1.8.3 -- still private in 1.12
        Field foodTickTimerField;
        int foodTickTimer;

        try
        {
            foodTickTimerField = FoodMetaData.class.getDeclaredField("foodTickTimer");
            foodTickTimerField.setAccessible(true);
            foodTickTimer = foodTickTimerField.getInt(entity.getFoodData());
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        playerNbt.setShort("Air", (short) entity.getAirTicks());
        // Health is now just a float; fractional is not stored separately.
        // (1.12)
        playerNbt.setShort("HurtTime", (short) entity.hurtTicks);
        playerNbt.setInt("HurtByTimestamp", entity.hurtTimestamp);
        playerNbt.setFloat("Health", entity.getHealth());
        playerNbt.setFloat("AbsorptionAmount", entity.getAbsorptionHearts());
        playerNbt.setInt("XpTotal", entity.expTotal);
        playerNbt.setInt("foodLevel", entity.getFoodData().foodLevel);
        playerNbt.setInt("foodTickTimer", foodTickTimer);
        playerNbt.setFloat("foodSaturationLevel", entity.getFoodData().saturationLevel);
        playerNbt.setFloat("foodExhaustionLevel", entity.getFoodData().exhaustionLevel);
        playerNbt.setShort("Fire", (short) entity.fireTicks);
        playerNbt.set("Inventory", npcPlayer.inventory.a(new NBTTagList()));

        File file1 = new File(worldStorage.getPlayerDir(), identity.getId().toString() + ".dat.tmp");
        File file2 = new File(worldStorage.getPlayerDir(), identity.getId().toString() + ".dat");

        try
        {
            NBTCompressedStreamTools.a(playerNbt, new FileOutputStream(file1));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to save player data for " + identity.getName(), e);
        }

        if ((!file2.exists() || file2.delete()) && !file1.renameTo(file2)) { throw new RuntimeException("Failed to save player data for " + identity.getName()); }
    }

    public static void createPlayerList(Player player)
    {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        for (WorldServer worldServer : getServer().worlds)
        {
            for (Object o : worldServer.players)
            {
                if (!(o instanceof NPCPlayer)) continue;

                NPCPlayer npcPlayer = (NPCPlayer) o;
                PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npcPlayer);
                p.playerConnection.sendPacket(packet);
            }
        }
    }

    public static void removePlayerList(Player player)
    {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        for (WorldServer worldServer : getServer().worlds)
        {
            for (Object o : worldServer.players)
            {
                if (!(o instanceof NPCPlayer)) continue;

                NPCPlayer npcPlayer = (NPCPlayer) o;
                PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npcPlayer);
                p.playerConnection.sendPacket(packet);
            }
        }
    }

    public static MinecraftServer getServer()
    {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

}
