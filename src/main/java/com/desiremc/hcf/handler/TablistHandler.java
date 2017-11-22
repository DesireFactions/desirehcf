package com.desiremc.hcf.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

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
import com.desiremc.core.tablist.PlayerList;
import com.desiremc.hcf.util.FactionsUtils;
import com.massivecraft.factions.FPlayer;

public class TablistHandler implements Listener
{

    private static HashMap<UUID, PlayerList> lists = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Session s;
        for (Iterator<Session> it = SessionHandler.getInstance().getSessions().iterator(); it.hasNext();)
        {
            s = it.next();
            if (s.getPlayer() == null || !s.getPlayer().isOnline())
            {
                it.remove();
                continue;
            }
            if (s.getSetting(SessionSetting.CLASSICTAB))
            {
                applyClassic(s.getPlayer());
            }
            else
            {
                applyFactions(s.getPlayer());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Session s;
        for (Iterator<Session> it = SessionHandler.getInstance().getSessions().iterator(); it.hasNext();)
        {
            s = it.next();
            if (s.getPlayer() == null || !s.getPlayer().isOnline())
            {
                it.remove();
                continue;
            }
            if (s.getSetting(SessionSetting.CLASSICTAB))
            {
                applyClassic(s.getPlayer());
            }
            else
            {
                applyFactions(s.getPlayer());
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
        PlayerList list = getPlayerList(player);
        for (FPlayer fp : FactionsUtils.getOnlineFPlayers())
        {
            //list.updateLocation(i, (fp.getFaction().isNormal() ? FactionsUtils.getRelationshipColor(user.getRelationTo(fp)) : ChatColor.YELLOW) + fp.getPlayer().getName());

            i++;
        }
    }

    private void applyFactions(Player player)
    {
        applyClassic(player);
    }

    private static PlayerList getPlayerList(Player player)
    {
        PlayerList list = lists.get(player.getUniqueId());
        if (list == null)
        {
            list = new PlayerList(player);
            list.initTable();
            lists.put(player.getUniqueId(), list);
        }
        return list;
    }

}
