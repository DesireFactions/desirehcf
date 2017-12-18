package com.desiremc.hcf.handler;

import java.util.Iterator;

import org.bukkit.Bukkit;
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
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tablist.TabList;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;

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
        for (Iterator<Session> it = SessionHandler.getSessions().iterator(); it.hasNext();)
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
        for (Iterator<Session> it = SessionHandler.getSessions().iterator(); it.hasNext();)
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

    private void applyClassic(Player player, Player ignored)
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                // get or initialize the player's tab list
                TabList list = getTabList(player);

                // if the player is on 1.8, ignore them
                if (!list.isOld())
                {
                    return;
                }

                // get the faction's player store
                HCFSession user = HCFSessionHandler.getHCFSession(player.getUniqueId());
                if (user == null)
                {
                    return;
                }

                // remove all existing player slots. This is annoying to do, but necessary for now.
                list.clearAllSlots();

                // used to count up the player slots
                int i = 0;

                // set all online players.
                for (HCFSession session : FactionsUtils.getOnlineHCFSessions())
                {
                    if (session.getPlayer() == ignored)
                    {
                        continue;
                    }
                    String prefix = null, name, suffix = "";
                    ChatColor color;
                    if (!session.hasFaction() || !user.hasFaction())
                    {
                        color = ChatColor.YELLOW;
                    }
                    else
                    {
                        color = session.getFaction().getRelationshipTo(user.getFaction()).getChatColor();
                    }
                    String str = color + session.getName();

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

                // update the player list
                list.update();
            }
        });
    }

    private void applyFactions(Player player, Player ignored)
    {
        applyClassic(player, ignored);
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
