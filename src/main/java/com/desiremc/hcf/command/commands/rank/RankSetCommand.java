package com.desiremc.hcf.command.commands.rank;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.api.RankAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.parser.RankParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;

public class RankSetCommand extends ValidCommand
{

    public RankSetCommand()
    {
        super("set", "Sets a user's rank.", Rank.ADMIN, new String[]{"target", "rank"}, "update");
        addParser(new PlayerSessionParser(), "target");
        addParser(new RankParser(), "rank");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {

        Session target = (Session) args[0];
        Rank rank = (Rank) args[1];

        RankAPI.setRank(sender, label, target, rank);
    }
}
