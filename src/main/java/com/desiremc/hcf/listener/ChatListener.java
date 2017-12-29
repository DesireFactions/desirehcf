package com.desiremc.hcf.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;

public class ChatListener implements Listener
{

    private static final boolean DEBUG = false;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event)
    {
        if (DEBUG)
        {
            System.out.println("ChatListener.onChat() called.");
        }
        if (event.isCancelled())
        {
            return;
        }
        Player player = event.getPlayer();
        event.setCancelled(true);
        Session s = SessionHandler.getSession(player);
        if (s.isMuted() != null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            player.sendMessage("");
            ChatUtils.sendCenteredMessage(player, DesireHCF.getLangHandler().getPrefix().replace(" ", ""));
            player.sendMessage("");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            return;
        }

        String msg = event.getMessage();

        Faction f = FactionsUtils.getFaction(player);

        String parsedMessage = s.getRank().getId() >= Rank.ADMIN.getId() ? ChatColor.translateAlternateColorCodes('&', msg) : msg;

        FancyMessage message = new FancyMessage(f.isWilderness() ? "§8[§b*§8] " : "§8[§b" + f.getName() + "§8] ")
                .then(s.getRank().getPrefix())
                .then(player.getName())
                .color(s.getRank().getMain())
                .tooltip(FactionsUtils.getMouseoverDetails(f))
                .then(": ")
                .then(parsedMessage)
                .color(s.getRank().getColor());

        Bukkit.getOnlinePlayers().stream().forEach(p -> message.send(p));
    }

}
