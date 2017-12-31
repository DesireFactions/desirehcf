package com.desiremc.hcf.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.desiremc.core.api.FileHandler;
import com.desiremc.hcf.DesireHCF;

public class SpawnHandler implements Listener
{
    private Location spawn;
    private static List<UUID> firstJoin = new ArrayList<>();
    private static SpawnHandler instance;

    public SpawnHandler()
    {
        instance = this;
    }

    public static SpawnHandler getInstance()
    {
        return instance;
    }

    public Location getSpawn()
    {
        if (spawn == null)
        {
            FileHandler config = DesireHCF.getConfigHandler();
            spawn = new Location(Bukkit.getWorld(config.getString("spawn.world")),
                    config.getDouble("spawn.x"),
                    config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"),
                    config.getDouble("spawn.yaw").floatValue(),
                    config.getDouble("spawn.pitch").floatValue());
        }
        return spawn;
    }

    public void setSpawn(Location spawn)
    {
        getInstance().spawn = spawn;
    }

    public void addPlayer(Player player)
    {
        if (!firstJoin.contains(player.getUniqueId()))
        {
            firstJoin.add(player.getUniqueId());
        }
    }

    public void addPlayer(UUID uuid)
    {
        if (!firstJoin.contains(uuid))
        {
            firstJoin.add(uuid);
        }
    }

    public void removePlayer(Player player)
    {
        firstJoin.remove(player.getUniqueId());
    }

    public boolean getPlayer(Player player)
    {
        return firstJoin.contains(player.getUniqueId());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();

        player.teleport(getSpawn());
    }
}
