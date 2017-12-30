package com.desiremc.hcf.listener.factions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.koth.events.NewCapperKothEvent;

public class TrophyListener implements Listener
{

    @EventHandler
    public void onKothCapture(NewCapperKothEvent event)
    {
        FSession session = event.getPlayerSession();
        Faction faction = session.getFaction();

        if (!faction.isWilderness())
        {
            faction.addTrophyPoints(125);
            faction.save();

            //TODO Add an EOTW mode, and if the KOTH is during EOTW, add 500 points instead.
        }

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.trophies.koth", true, false, "{points}", 125));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (event.getEntity().getKiller() == null)
        {
            return;
        }

        FSession killer = FSessionHandler.getFSession(event.getEntity().getKiller());
        FSession player = FSessionHandler.getFSession(event.getEntity());

        if (player.getFaction().isWilderness() || killer.getFaction().isWilderness())
        {
            return;
        }

        if (player.getFaction().getTrophyPoints() < 5)
        {
            return;
        }

        player.getFaction().removeTrophyPoints(5);
        killer.getFaction().addTrophyPoints(5);

        killer.getFaction().broadcast(DesireHCF.getLangHandler().renderMessage("factions.trophies.kill", true, false, "{points}", 5, "{player}", player.getName(), "{killer}", killer.getName()));
        player.getFaction().broadcast(DesireHCF.getLangHandler().renderMessage("factions.trophies.death", true, false, "{points}", 5, "{player}", player.getName(), "{killer}", killer.getName()));

        if (player.getFaction().isRaidable())
        {
            if ((player.getFaction().getDTR() + 1) > 0)
            {
                //they just went raidable
                player.getFaction().setTrophyPoints(0);
                player.getFaction().save();
                player.getFaction().broadcast(DesireHCF.getLangHandler().renderMessage("factions.trophies.raidable", true, false));
            }
        }

        player.getFaction().save();
        killer.getFaction().save();

    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
        if (!event.getBlock().getType().equals(Material.DIAMOND_ORE) && !event.getBlock().getType().equals(Material.EMERALD_ORE))
        {
            return;
        }

        FSession player = FSessionHandler.getFSession(event.getPlayer());
        Faction faction = player.getFaction();

        if (player.getFaction().isWilderness())
        {
            return;
        }

        faction.addTrophyPoints(0.5);
        faction.save();

        String ore = StringUtils.capitalize(event.getBlock().getType().name().replace("_", " ").toLowerCase());

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.trophies.mine", true, false, "{points}", 0.5, "{ore}", ore));
    }
}
