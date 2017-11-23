package com.desiremc.hcf.handler;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.tablistfive.TabAPI;
import com.desiremc.core.tablistfive.TabList;
import com.desiremc.hcf.util.FactionsUtils;
import com.massivecraft.factions.FPlayer;

public class TablistHandler implements Listener
{

    private static final boolean DEBUG = true;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        if (DEBUG)
        {
            System.out.println("TablistHandler.onJoin() called.");
        }
        Session iterSession;
        for (Iterator<Session> it = SessionHandler.getInstance().getSessions().iterator(); it.hasNext();)
        {
            iterSession = it.next();
            if (DEBUG)
            {
                System.out.println("TablistHandler.onJoin() iterated with " + iterSession.getName());
            }
            if (iterSession.getPlayer() == null || !iterSession.getPlayer().isOnline())
            {
                if (DEBUG)
                {
                    System.out.println("TablistHandler.onJoin() removed offline or null player.");
                }
                it.remove();
                continue;
            }
            if (iterSession.getSetting(SessionSetting.CLASSICTAB))
            {
                applyClassic(iterSession.getPlayer());
            }
            else
            {
                applyFactions(iterSession.getPlayer());
            }
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        TabAPI.removePlayer(event.getPlayer());
        Session iterSession;
        for (Iterator<Session> it = SessionHandler.getInstance().getSessions().iterator(); it.hasNext();)
        {
            iterSession = it.next();
            if (iterSession.getPlayer() == null || !iterSession.getPlayer().isOnline())
            {
                it.remove();
                continue;
            }
            if (iterSession.getSetting(SessionSetting.CLASSICTAB))
            {
                clearClassic(iterSession.getPlayer(), event.getPlayer());
            }
            else
            {
                clearFactions(iterSession.getPlayer(), event.getPlayer());
            }
        }
    }

    private void applyClassic(Player player)
    {
        FPlayer user = FactionsUtils.getFPlayer(player);
        if (user == null)
        {
            return;
        }
        int i = 0;
        TabList list = getTabList(player);

        for (FPlayer fp : FactionsUtils.getOnlineFPlayers())
        {
            String prefix = null, name, suffix = "";
            String str = (fp.getFaction().isNormal() ? FactionsUtils.getRelationshipColor(user.getRelationTo(fp)) : ChatColor.YELLOW) + fp.getPlayer().getName();

            if (str.length() <= 16)
            {
                name = str;
            }
            else if (str.length() > 16 && str.length() <= 32 && str.charAt(15) != '§')
            {
                prefix = str.substring(0, str.length() - 16);
                name = str.substring(str.length() - 16);
            }
            else
            {
                prefix = str.substring(0, 16);
                name = str.substring(16, 32);
                suffix = str.substring(32);
            }
            if (prefix != null)
            {
                list.setSlot(i, prefix, name, suffix);
            }
            else
            {
                list.setSlot(i, name);
            }
            i++;
        }
        list.send();
    }

    private void applyFactions(Player player)
    {
        applyClassic(player);
    }

    private void clearClassic(Player updated, Player cleared)
    {
        getTabList(updated).clearSlot(cleared.getName());
        getTabList(updated).send();
    }

    private void clearFactions(Player updated, Player changed)
    {
        this.clearClassic(updated, changed);
    }

    private static TabList getTabList(Player player)
    {
        if (DEBUG)
        {
            System.out.println("TablistHandler.getTabList(Player) called.");
        }
        TabList list = TabAPI.getPlayerTabList(player);
        if (list == null)
        {
            if (DEBUG)
            {
                System.out.println("TablistHandler.getTabList(Player) it's null, populate.");
            }
            list = TabAPI.createTabListForPlayer(player);
            list.send();
        }
        if (DEBUG)
        {
            System.out.println("TablistHandler.getTabList(Player) return the list.");
        }
        return list;
    }

}
