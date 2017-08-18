package me.borawski.hcf.listener;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.FactionSessionHandler;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;
import mkremins.fanciful.FancyMessage;

public class ChatListener implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Session s = SessionHandler.getSession(event.getPlayer());
        if (s.isMuted() != null) {
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), Core.getInstance().getPrefix().replace(" ", ""));
            s.sendMessage("");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(event.getPlayer(), ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            s.sendMessage("");
            s.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            return;
        }

        String msg = event.getMessage();
        Player player = event.getPlayer();

        Bukkit.getOnlinePlayers().stream().forEach(new Consumer<Player>() {
            @Override
            public void accept(Player players) {
                Session s = SessionHandler.getSession(player);
                Faction f = MPlayer.get(player).getFaction();

                if (f == null) {
                    new FancyMessage(s.getRank().getPrefix() + " " + player.getName() + ": " + s.getRank().getColor() + msg).tooltip(new String[] {
                            ChatColor.RED + "User is not in a faction"
                    }).send(players);
                    return;
                }

                FactionSession fSession = FactionSessionHandler.getFactionSession(f.getName());

                new FancyMessage(s.getRank().getPrefix())
                        .then(player.getName())
                            .tooltip(new String[] {
                                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                                    ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + (f != null && fSession != null ? fSession.getName() : "NONE"),
                                    ChatColor.GRAY + "Members: " + ChatColor.YELLOW + "" + (f != null && fSession != null ? f.getMPlayers().size() : "NONE"),
                                    ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + "" + (f != null && fSession != null ? fSession.getTrophies() : "NONE")
                            })
                        .then(": " + s.getRank().getColor() + msg)
                        .send(players);
            }
        });
    }

}
