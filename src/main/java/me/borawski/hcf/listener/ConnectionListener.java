package me.borawski.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.borawski.hcf.Core;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.DateUtils;
import me.borawski.hcf.util.PlayerUtils;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        Session session = SessionHandler.getSession(event.getPlayer());
        Punishment p;
        if ((p = session.isBanned()) != null) {

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, (Core.getInstance().getPrefix() + "\n" + "\n" + "&c&lYou are banned from the network!\n" + "\n" + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n" + "\n" + "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules").replace("{reason}", p.getReason())
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
        Core.getStaffHandler().disableStaffMode(e.getPlayer());
        Core.getStaffHandler().unfreezePlayer(e.getPlayer());
        // TagHandler.lastValidLocation.remove(e.getPlayer().getUniqueId());
        // Core.getTagHandler().clearTag(e.getPlayer().getUniqueId());
        // Core.getHCFPlayerHandler().unloadPlayer(e.getPlayer());
        e.setQuitMessage(Core.getLangHandler().getString("leave.message").replace("{player}", e.getPlayer().getName()));
    }

}
