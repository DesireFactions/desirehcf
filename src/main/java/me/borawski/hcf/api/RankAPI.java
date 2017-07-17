package me.borawski.hcf.api;

import org.bukkit.Bukkit;
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

    private static final LangHandler LANG = Core.getLangHandler();

    /**
     * Change a player's {@link Rank}
     * 
     * @param sender
     * @param label
     * @param name
     * @param rank
     */
    public static void setRank(CommandSender sender, String label, String name, String rank, boolean display) {
        Session s = SessionHandler.getSession(name);

        if (s == null) {
            sender.sendMessage("[Core] [ERROR] : Could not retrieve " + name);
            return;
        }

        if (Rank.getRank(rank) == null) {
            LANG.sendString(sender, "rank.invalid");
            return;
        }

        s.setRank(Rank.getRank(rank));
        SessionHandler.getInstance().save(s);
        LANG.sendRenderMessage(sender, "rank.set",
                "{player}", s.getName(),
                "{rank}", s.getRank().getDisplayName());

        if (Bukkit.getPlayer(s.getUniqueId()) != null && display) {
            PlayerUtils.setPrefix(s.getRank().getPrefix(), Bukkit.getPlayer(s.getUniqueId()));
            LANG.sendRenderMessage(Bukkit.getPlayer(s.getUniqueId()), "rank.inform",
                    "{rank}", s.getRank().getDisplayName());
        }
    }

    /**
     * Send a player a list of every {@link Rank}
     * 
     * @param sender
     */
    public static void listRanks(CommandSender sender) {
        for (Rank r : Rank.values()) {
            LANG.sendRenderMessage(sender, "rank.list",
                    "{color}", r.getColor().toString(),
                    "{rank}", r.getDisplayName());
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
            LANG.sendString(sender, "only-players");
            return;
        }

        Session s = null;
        try {
            s = SessionHandler.getSession(((Player) sender).getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LANG.sendRenderMessage(sender, "rank.set",
                "{color}", s.getRank().toString(),
                "{rank}", s.getRank().getDisplayName());
    }

}