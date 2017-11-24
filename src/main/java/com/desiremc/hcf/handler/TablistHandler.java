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
import com.desiremc.core.tablistsix.TabAPI;
import com.desiremc.core.tablistsix.TabList;
import com.desiremc.hcf.util.FactionsUtils;
import com.massivecraft.factions.FPlayer;

public class TablistHandler implements Listener
{

    private static final boolean DEBUG = false;

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
                applyClassic(iterSession.getPlayer(), null);
            }
            else
            {
                applyFactions(iterSession.getPlayer(), null);
            }
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Session iterSession;
        for (Iterator<Session> it = SessionHandler.getInstance().getSessions().iterator(); it.hasNext();)
        {
            iterSession = it.next();
            if (iterSession.getPlayer() == null || !iterSession.getPlayer().isOnline())
            {
                it.remove();
                continue;
            }
            if (event.getPlayer() != iterSession.getPlayer())
            {
                if (iterSession.getSetting(SessionSetting.CLASSICTAB))
                {
                    applyClassic(iterSession.getPlayer(), event.getPlayer());
                }
                else
                {
                    applyFactions(iterSession.getPlayer(), event.getPlayer());
                }
            }
        }
        TabAPI.removePlayer(event.getPlayer());
    }

    private TabList applyClassic(Player player, Player ignored)
    {
        FPlayer user = FactionsUtils.getFPlayer(player);
        if (user == null)
        {
            return null;
        }
        int i = 0;
        TabList list = getTabList(player);
        if (!list.isOld())
        {
            return null;
        }
        list.clearAllSlots();

        for (FPlayer fp : FactionsUtils.getOnlineFPlayers())
        {
            if (fp.getPlayer() == ignored)
            {
                continue;
            }
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
        list.update();
        return list;
    }

    private TabList applyFactions(Player player, Player ignored)
    {
        return applyClassic(player, ignored);
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
