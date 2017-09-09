package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.session.Rank;

public class RankParser implements ArgumentParser {

    @Override
    public Rank parseArgument(CommandSender sender, String label, String arg) {
        Rank rank = Rank.getRank(arg);

        if (rank == null) {
            LANG.sendString(sender, "rank.invalid");
            return null;
        }

        return rank;
    }

}
