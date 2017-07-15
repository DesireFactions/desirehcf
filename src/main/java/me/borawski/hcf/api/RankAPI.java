package me.borawski.hcf.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.PlayerUtils;

/**
 * @author Ryan Radomski
 *
 */
public class RankAPI {

    /**
     * Change a player's {@link Rank}
     * 
     * @param sender
     * @param label
     * @param name
     * @param rank
     */
    public static void setRank(CommandSender sender, String label, String name, String rank, boolean display) {
        Session s = SessionHandler.getSession(PlayerUtils.getUUIDFromName(name));

        if (s == null) {
            sender.sendMessage("[Core] [ERROR] : Could not retrieve " + name);
            return;
        }

        if (Rank.valueOf(rank) == null) {
            sender.sendMessage("Invalid rank");
            return;
        }

        s.setRank(Rank.valueOf(rank));
        SessionHandler.getInstance().save(s);
        sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF " + ChatColor.RESET + "" + ChatColor.GRAY + "You have set " + ChatColor.YELLOW + name + "" + ChatColor.GRAY + "'s rank to " + ChatColor.YELLOW + rank);

        if (Bukkit.getPlayer(s.getUniqueId()) != null && display) {
            PlayerUtils.setPrefix(s.getRank().getPrefix(), Bukkit.getPlayer(s.getUniqueId()));
            s.sendMessage(ChatColor.GREEN + "You are now a " + s.getRank().name().toUpperCase());
        }
    }

    /**
     * Send a player a list of every {@link Rank}
     * 
     * @param sender
     */
    public static void listRanks(CommandSender sender) {
        for (Rank r : Rank.values()) {
            sender.sendMessage(r.getColor() + r.getDisplayName());
        }
    }

    /**
     * Send a player their current {@link Rank}
     * 
     * @param sender
     * @param label
     */
    public static void checkRank(CommandSender sender, String label) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Core.getLangHandler().getString("only-players"));
            return;
        }

        Session s = null;
        try {
            s = SessionHandler.getSession(((Player) sender).getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sender.sendMessage(ChatColor.DARK_RED + "" +
                ChatColor.BOLD + "DesireHCF " +
                ChatColor.RESET + "" +
                ChatColor.GRAY + "Your rank is currently " +
                ChatColor.YELLOW + s.getRank().getDisplayName());
    }

}