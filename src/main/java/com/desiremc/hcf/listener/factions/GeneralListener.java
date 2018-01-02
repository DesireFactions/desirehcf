package com.desiremc.hcf.listener.factions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionType;
import com.desiremc.hcf.util.FactionsUtils;

public class GeneralListener implements Listener
{

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlock().getType() == Material.TNT)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event)
    {
        if (!(event.getEntity() instanceof Animals))
        {
            if (event.getEntity().getType() != EntityType.CREEPER && event.getEntity().getType() != EntityType.ENDERMAN
                    && event.getEntity().getLocation().getWorld().getEnvironment() != World.Environment.THE_END)
            {
                event.setCancelled(true);
            }
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
    public void onTarget(EntityTargetEvent event)
    {
        if (event.getEntityType().equals(EntityType.CREEPER))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event)
    {
        event.setCancelled(event.toWeatherState());
    }

    @EventHandler
    public void onTeleport(PlayerPortalEvent event)
    {
        Player player = event.getPlayer();
        FSession fSession = FSessionHandler.getOnlineFSession(player.getUniqueId());

        if (fSession.hasClaimSession())
        {
            fSession.clearClaimSession();
            DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.cancel", true, false);
        }
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event)
    {
        Block block = event.getBlock();
        if (block.getType() == Material.WATER || block.getType() == Material.LAVA)
        {
            Location loc = event.getToBlock().getLocation();
            Faction faction = FactionHandler.getFaction(loc);
            if (faction.getType() != FactionType.PLAYER)
            {
                event.setCancelled(true);
            }
        }
    }
}
