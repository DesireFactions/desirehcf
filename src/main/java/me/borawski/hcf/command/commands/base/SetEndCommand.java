package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.command.commands.sub.SetEndExitCommand;
import me.borawski.hcf.command.commands.sub.SetEndSpawnCommand;
import me.borawski.hcf.session.Rank;

public class SetEndCommand extends ValidBaseCommand {

    public SetEndCommand() {
        super("setend", "Sets end spawn and exit", Rank.ADMIN);
        addSubCommand(new SetEndSpawnCommand());
        addSubCommand(new SetEndExitCommand());
    }

}
