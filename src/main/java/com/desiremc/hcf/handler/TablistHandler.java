package com.desiremc.hcf.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tablist.TabList;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.util.FactionsUtils;

public class TablistHandler implements Listener
{

    private HashMap<UUID, Set<Player>> classicTabs;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Player joiner = event.getPlayer();
        for (Session session : SessionHandler.getOnlineSessions())
        {
            TabList tabList;
            if (event.getPlayer() == session.getPlayer())
            {
                tabList = TabAPI.createTabListForPlayer(joiner);
                FSession fSession = FSessionHandler.getOnlineFSession(joiner.getUniqueId());
                if (session.getSetting(SessionSetting.CLASSICTAB))
                {
                    Set<Player> players = new HashSet<>();
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        tabList.setSlot(players.size(), FactionsUtils.getFaction(player).getRelationshipTo(fSession.getFaction()).getChatColor() + player.getName());
                        players.add(player);
                    }
                    classicTabs.put(joiner.getUniqueId(), players);
                }
                else
                {
                    tabList.setSlot(0, 1, "§3§l" + DesireCore.getCurrentServer());

                    tabList.setSlot(1, 0, "§b§lPlayer Info");
                    tabList.setSlot(2, 0, "§bKills: §c" + fSession.getTotalKills());
                    tabList.setSlot(3, 0, "§bDeaths: §c" + fSession.getTotalDeaths());

                    tabList.setSlot(5, 0, "§b§lLocation:");
                    tabList.setSlot(6, 0, FactionHandler.getFaction(event.getPlayer().getLocation()).getName());
                    tabList.setSlot(7, 0, "§8(§7" + joiner.getLocation().getBlockX() + "§8," + joiner.getLocation().getBlockZ() + "§8)");

                    tabList.setSlot(1, 2, "§b§lEnd Portals:");
                    tabList.setSlot(2, 2, "§7(1000,1000)");
                    tabList.setSlot(3, 2, "In each quadrant");

                    tabList.setSlot(5, 2, "§b§lWorld Border:");
                    tabList.setSlot(6, 2, "§7±3000");

                    tabList.setSlot(5, 2, "§b§lPlayers Online:");
                    tabList.setSlot(6, 2, "§7" + Bukkit.getOnlinePlayers().size());
                }
            }
            else if (session.getSetting(SessionSetting.CLASSICTAB))
            {
                Set<Player> players = classicTabs.get(session.getUniqueId());
                tabList = TabAPI.getPlayerTabList(session.getPlayer());
                tabList.setSlot(players.size(), FactionsUtils.getFaction(joiner).getRelationshipTo(FSessionHandler.getOnlineFSession(session.getUniqueId()).getFaction()).getChatColor() + joiner.getName());
                players.add(joiner);
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
                classicTabs.remove(leaver.getUniqueId());
            }
            else if (session.getSetting(SessionSetting.CLASSICTAB))
            {
                Set<Player> players = classicTabs.get(session.getUniqueId());
                tabList = TabAPI.getPlayerTabList(session.getPlayer());
                int i = 0;
                for (Player player : players)
                {
                    if (player == event.getPlayer())
                    {
                        break;
                    }
                    i++;
                }
                tabList.setSlot(i, "");
                players.remove(leaver);
            }
        }
        TabAPI.removePlayer(event.getPlayer());
    }

}
