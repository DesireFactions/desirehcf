package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.commands.sub.SetEndExitCommand;
import me.borawski.hcf.command.commands.sub.SetEndSpawnCommand;
import me.borawski.hcf.session.Rank;

public class SetEndCommand extends CustomBaseCommand {

    public SetEndCommand() {
        super("setend", "sets ends spawn or exit", Rank.ADMIN);
        addSubCommand(new SetEndSpawnCommand());
        addSubCommand(new SetEndExitCommand());
    }

}
