package com.desiremc.hcf.commands.setend;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class SetEndCommand extends ValidBaseCommand {

    public SetEndCommand() {
        super("setend", "Sets end spawn and exit", Rank.ADMIN);
        addSubCommand(new SetEndSpawnCommand());
        addSubCommand(new SetEndExitCommand());
    }

}
