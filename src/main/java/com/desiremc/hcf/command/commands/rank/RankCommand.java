package com.desiremc.hcf.command.commands.rank;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class RankCommand extends ValidBaseCommand {

    public RankCommand() {
        super("rank", "View your rank or manage others.", Rank.GUEST);
        addSubCommand(new RankCheckCommand());
        addSubCommand(new RankSetCommand());
        addSubCommand(new RankListCommand());
    }

}
