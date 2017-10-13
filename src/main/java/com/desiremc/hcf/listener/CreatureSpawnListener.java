package com.desiremc.hcf.listener;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.desiremc.core.DesireCore;

public class CreatureSpawnListener implements Listener
{

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e)
    {
        if (e.getSpawnReason() == SpawnReason.NATURAL)
        {
            if (e.getEntity().getWorld().getEnvironment() == World.Environment.THE_END)
            {
                e.getEntity().remove();
            } else
            {
                if (e.getEntity().getType() == EntityType.CREEPER || e.getEntity().getType() == EntityType.ENDERMAN)
                {
                    if (!DesireCore.getConfigHandler().getBoolean("spawn-mobs"))
                    {
                        e.getEntity().remove();
                    }
                }
            }
        }
    }
}