package com.desiremc.hcf.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.PlayerUtils;

/**
 * @author Ryan Radomski
 *
 */
public class RankAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    /**
     * Change a player {@link Session}'s {@link Rank}
     * 
     * @param sender
     * @param label
     * @param name
     * @param rank
     */
    public static void setRank(CommandSender sender, String label, Session taget, Rank rank, boolean display)
    {
        taget.setRank(rank);

        SessionHandler.getInstance().save(taget);
        LANG.sendRenderMessage(sender, "rank.set",
                "{player}", taget.getName(),
                "{rank}", taget.getRank().getDisplayName());

        if (Bukkit.getPlayer(taget.getUniqueId()) != null && display)
        {
            PlayerUtils.setPrefix(taget.getRank().getPrefix(), Bukkit.getPlayer(taget.getUniqueId()));
            LANG.sendRenderMessage(Bukkit.getPlayer(taget.getUniqueId()), "rank.inform",
                    "{rank}", taget.getRank().getDisplayName());
        }
    }

    /**
     * Change a player {@link Session}'s {@link Rank}
     * 
     * @param sender
     * @param label
     * @param name
     * @param rank
     */
    public static void setRank(CommandSender sender, String label, Session target, Rank rank)
    {
        setRank(sender, label, target, rank, false);
    }

    /**
     * Send a player a list of every {@link Rank}
     * 
     * @param sender
     */
    public static void listRanks(CommandSender sender)
    {
        for (Rank r : Rank.values())
        {
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
    public static void checkRank(CommandSender sender, String label)
    {
        Session s = SessionHandler.getSession(((Player) sender).getUniqueId());

        LANG.sendRenderMessage(sender, "rank.check",
                "{color}", s.getRank().getColor().toString(),
                "{rank}", s.getRank().getDisplayName());
    }

}