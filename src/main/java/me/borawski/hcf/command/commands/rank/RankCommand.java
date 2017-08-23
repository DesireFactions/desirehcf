package me.borawski.hcf.command.commands.rank;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class RankCommand extends ValidBaseCommand {

    public RankCommand() {
        super("rank", "View your rank or manage others.", Rank.GUEST);
        addSubCommand(new RankCheckCommand());
        addSubCommand(new RankSetCommand());
        addSubCommand(new RankListCommand());
    }

}
