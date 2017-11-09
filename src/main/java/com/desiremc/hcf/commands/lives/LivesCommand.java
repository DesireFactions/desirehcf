package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class LivesCommand extends ValidBaseCommand
{

    public LivesCommand()
    {
        super("lives", "Add or remove lives.", Rank.MODERATOR);
        addSubCommand(new LivesAddCommand());
        addSubCommand(new LivesRemoveCommand());
        addSubCommand(new LivesCheckCommand());
        addSubCommand(new LivesUseCommand());
    }

}
