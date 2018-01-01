package com.desiremc.hcf.listener.factions;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;

public class GeneralListener implements Listener
{

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlock().getLocation().getWorld().getEnvironment() == World.Environment.THE_END)
        {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getType() == Material.TNT)
        {
            event.setCancelled(true);
            return;
        }

        Faction faction = FactionsUtils.getFaction(event.getBlock().getLocation());
        FSession session = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        if ((faction.isNormal() && faction.getMembers().contains(session)) || faction.isWilderness())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getLocation().getWorld().getEnvironment() == World.Environment.THE_END)
        {
            event.setCancelled(true);
            return;
        }

        Faction faction = FactionsUtils.getFaction(event.getBlock().getLocation());
        FSession session = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        if ((faction.isNormal() && faction.getMembers().contains(session)) || faction.isWilderness())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event)
    {
        if (!(event.getEntity() instanceof Animals))
        {
            event.setCancelled(true);
            return;
        }

        Faction faction = FactionsUtils.getFaction(event.getLocation());

        if (faction.isNormal() || faction.isWilderness())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onTntExplode(EntityExplodeEvent event)
    {
        event.blockList().clear();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().getType().equals(Material.FISHING_ROD))
        {
            return;
        }

        Player player = event.getPlayer();
        Faction faction = FactionsUtils.getFaction(player.getLocation());
        FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());

        if ((faction.isNormal() && faction.getMembers().contains(session)) || faction.isWilderness())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event)
    {
        if (event.getEntityType().equals(EntityType.CREEPER))
        {
            event.setCancelled(true);
        }
    }
}
