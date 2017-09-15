package com.desiremc.hcf.command.commands.lives;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class LivesCommand extends ValidBaseCommand
{

    public LivesCommand()
    {
        super("lives", "Add or remove lives.", Rank.MODERATOR);
        addSubCommand(new LivesAddCommand());
        addSubCommand(new LivesRemoveCommand());
    }

}
