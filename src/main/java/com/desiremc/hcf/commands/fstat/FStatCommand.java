package com.desiremc.hcf.commands.fstat;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class FStatCommand extends ValidBaseCommand
{

    public FStatCommand()
    {
        super("fstat", "View your player stats");

        addSubCommand(new FStatShowCommand());
        addSubCommand(new FStatFactionCommand());
    }
}
