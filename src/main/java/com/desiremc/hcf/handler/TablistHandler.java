package com.desiremc.hcf.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.events.PlayerBlockMoveEvent;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tablist.TabList;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.util.FactionsUtils;

public class TablistHandler implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Player joiner = event.getPlayer();
        for (Session session : SessionHandler.getOnlineSessions())
        {
            TabList tabList;
            if (event.getPlayer().getUniqueId().equals(session.getUniqueId()))
            {
                System.out.println("Created tablist");
                tabList = TabAPI.createTabListForPlayer(joiner);
                FSession fSession = FSessionHandler.getOnlineFSession(joiner.getUniqueId());

                tabList.setSlot(0, 1, "§3§l" + DesireCore.getCurrentServer());

                tabList.setSlot(1, 0, "§b§lPlayer Info");
                tabList.setSlot(2, 0, "§bKills: §c" + fSession.getTotalKills());
                tabList.setSlot(3, 0, "§bDeaths: §c" + fSession.getTotalDeaths());

                tabList.setSlot(5, 0, "§b§lLocation:");
                tabList.setSlot(6, 0, FactionHandler.getFaction(event.getPlayer().getLocation()).getName());
                tabList.setSlot(7, 0, "§8(§7" + joiner.getLocation().getBlockX() + "§8,§7" + joiner.getLocation().getBlockZ() + "§8)");

                tabList.setSlot(1, 2, "§b§lEnd Portals:");
                tabList.setSlot(2, 2, "§7(1000,1000)");
                tabList.setSlot(3, 2, "In each quadrant");

                tabList.setSlot(5, 2, "§b§lWorld Border:");
                tabList.setSlot(6, 2, "§7+-3000");

                tabList.setSlot(8, 2, "§b§lPlayers Online:");
                tabList.setSlot(9, 2, "§7" + Bukkit.getOnlinePlayers().size());

            }
            else
            {
                tabList = TabAPI.getPlayerTabList(session.getPlayer());
                tabList.setSlot(6, 2, "§7" + Bukkit.getOnlinePlayers().size());
            }
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player leaver = event.getPlayer();
        TabList tabList;
        for (Session session : SessionHandler.getOnlineSessions())
        {
            tabList = TabAPI.getPlayerTabList(session.getPlayer());
            if (session.getPlayer() == leaver)
            {
                tabList.terminate();
            }
            else
            {
                tabList.setSlot(6, 2, "§7" + Bukkit.getOnlinePlayers().size());
            }
        }
        TabAPI.removePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockMove(PlayerBlockMoveEvent event)
    {
        TabList tabList = TabAPI.getPlayerTabList(event.getPlayer());
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());
        if (fSession.getLastFactionLocation() == null)
        {
            fSession.setLastLocation(FactionsUtils.getFaction(event.getTo()));
        }
        tabList.setSlot(6, 0, fSession.getLastFactionLocation().getName());
        tabList.setSlot(7, 0, "§8(§7" + event.getTo().getBlockX() + "§8,§7" + event.getFrom().getBlockZ() + "§8)");
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        TabList tabList = TabAPI.getPlayerTabList(event.getPlayer());
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());
        if (fSession.getLastFactionLocation() == null)
        {
            Faction faction = FactionsUtils.getFaction(event.getTo());
            if (faction == null)
            {
                System.out.println("=============");
                System.out.println("FACTION WAS NULL FOR SOME REASON");
                System.out.println("=========");
            }
            fSession.setLastLocation(FactionsUtils.getFaction(event.getTo()));
        }
        tabList.setSlot(6, 0, fSession.getLastFactionLocation().getName());
        tabList.setSlot(7, 0, "§8(§7" + event.getTo().getBlockX() + "§8,§7" + event.getFrom().getBlockZ() + "§8)");
    }

    public static void updateKills(FSession fSession)
    {
        TabList tabList = TabAPI.getPlayerTabList(fSession.getPlayer());
        tabList.setSlot(2, 0, "§bKills: §c" + fSession.getTotalKills());
    }

}
