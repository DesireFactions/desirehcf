package me.borawski.hcf.command.commands.setend;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class SetEndCommand extends ValidBaseCommand {

    public SetEndCommand() {
        super("setend", "Sets end spawn and exit", Rank.ADMIN);
        addSubCommand(new SetEndSpawnCommand());
        addSubCommand(new SetEndExitCommand());
    }

}
