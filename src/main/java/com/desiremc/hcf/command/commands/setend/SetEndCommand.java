package com.desiremc.hcf.command.commands.setend;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class SetEndCommand extends ValidBaseCommand {

    public SetEndCommand() {
        super("setend", "Sets end spawn and exit", Rank.ADMIN);
        addSubCommand(new SetEndSpawnCommand());
        addSubCommand(new SetEndExitCommand());
    }

}
