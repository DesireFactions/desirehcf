package com.desiremc.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.punishment.Punishment;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.session.StaffHandler;
import com.desiremc.hcf.util.DateUtils;
import com.desiremc.hcf.util.PlayerUtils;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        Session session = SessionHandler.getSession(event.getPlayer());
        Punishment p;
        if ((p = session.isBanned()) != null) {

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, (DesireCore.getLangHandler().getPrefix() + "\n" + "\n" + "&c&lYou are banned from the network!\n" + "\n" + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n" + "\n" + "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules").replace("{reason}", p.getReason())
                    .replace("{until}", DateUtils.formatDateDiff(p.getExpirationTime())).replace("{issuer}", PlayerUtils.getName(p.getIssuer()).replace("&", "§")));
            return;
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SessionHandler.initializeSession(event.getPlayer(), true);
        Session session = SessionHandler.getSession(event.getPlayer());
        boolean noColor = session.getRank().getId() == 1;
        boolean justColor = session.getRank().getId() == 2;
        event.getPlayer().setPlayerListName(noColor ? ChatColor.GRAY + event.getPlayer().getName() : justColor ? session.getRank().getMain() + event.getPlayer().getName() : session.getRank().getPrefix() + " " + ChatColor.GRAY + event.getPlayer().getName());
    }

    @EventHandler
    public void logout(PlayerQuitEvent event) {
        Session session = SessionHandler.getSession(event.getPlayer());
        session.setTotalPlayed(session.getTotalPlayed() + (System.currentTimeMillis() - session.getLastLogin()));
        session.setLastLogin(System.currentTimeMillis());
        SessionHandler.endSession(session);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        StaffHandler.getInstance().disableStaffMode(e.getPlayer());
        StaffHandler.getInstance().unfreezePlayer(e.getPlayer());
        e.setQuitMessage(DesireCore.getLangHandler().getString("leave.message").replace("{player}", e.getPlayer().getName()));
    }

}
