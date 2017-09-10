package com.desiremc.hcf.command.commands.fstat;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class FStatCommand extends ValidBaseCommand {

    public FStatCommand() {
        super("fstat", "View your player stats", Rank.GUEST,
                new String[] {});
        addSubCommand(new FStatShowCommand());
        addSubCommand(new FStatFactionCommand());
    }
}
