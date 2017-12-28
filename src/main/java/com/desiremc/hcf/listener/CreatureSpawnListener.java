package com.desiremc.hcf.listener;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;

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
            }
            else
            {
                if (e.getEntity().getType() == EntityType.CREEPER || e.getEntity().getType() == EntityType.ENDERMAN)
                {
                    if (!DesireHCF.getConfigHandler().getBoolean("spawn-mobs"))
                    {
                        e.getEntity().remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event)
    {
        Faction faction = FactionHandler.getFaction(event.getTarget().getLocation());
        if (faction.isSafeZone())
        {
            event.setCancelled(true);
        }
    }

}