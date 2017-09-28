package com.desiremc.hcf.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class NPCPlayer extends EntityPlayer
{
    private NPCIdentity identity;

    private NPCPlayer(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager)
    {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
    }

    public NPCIdentity getNpcIdentity()
    {
        return identity;
    }

    public static NPCPlayer valueOf(Player player)
    {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
        PlayerInteractManager playerInteractManager = new PlayerInteractManager(worldServer);
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());

        for (Map.Entry<String, Property> entry : ((CraftPlayer) player).getProfile().getProperties().entries())
        {
            gameProfile.getProperties().put(entry.getKey(), entry.getValue());
        }

        NPCPlayer NPCPlayer = new NPCPlayer(minecraftServer, worldServer, gameProfile, playerInteractManager);
        NPCPlayer.identity = new NPCIdentity(player);

        new NPCPlayerConnection(NPCPlayer);

        return NPCPlayer;
    }

}
