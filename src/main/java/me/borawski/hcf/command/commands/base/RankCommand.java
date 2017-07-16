package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.commands.sub.RankCheckCommand;
import me.borawski.hcf.command.commands.sub.RankListCommand;
import me.borawski.hcf.command.commands.sub.RankSetCommand;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 3/12/2017.
 */
public class RankCommand extends CustomBaseCommand {

    public RankCommand() {
        super("rank", "View your rank or manage others.", Rank.GUEST);
        addSubCommand(new RankCheckCommand());
        addSubCommand(new RankSetCommand());
        addSubCommand(new RankListCommand());
    }

}
