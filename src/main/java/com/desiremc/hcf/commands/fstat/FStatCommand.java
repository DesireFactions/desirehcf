package com.desiremc.hcf.commands.fstat;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class FStatCommand extends ValidBaseCommand
{

    public FStatCommand()
    {
        super("fstat", "View your player stats", Rank.GUEST,
                new String[]{});
        addSubCommand(new FStatShowCommand());
        addSubCommand(new FStatFactionCommand());
    }
}
