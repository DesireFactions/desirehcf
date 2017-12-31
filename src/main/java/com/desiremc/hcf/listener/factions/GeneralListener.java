package com.desiremc.hcf.listener.factions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;

public class GeneralListener implements Listener
{

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlock().getType() == Material.TNT)
        {
            event.setCancelled(true);
            return;
        }

        Faction faction = FactionsUtils.getFaction(event.getBlock().getLocation());
        if (faction.isNormal())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Faction faction = FactionsUtils.getFaction(event.getBlock().getLocation());
        if (faction.isNormal())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event)
    {
        Faction faction = FactionsUtils.getFaction(event.getLocation());
        if (faction.isNormal())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onTNTBreak(EntityExplodeEvent event)
    {
        event.blockList().clear();
    }
}
