package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class KitManagementCommand extends ValidBaseCommand
{

    public KitManagementCommand()
    {
        super("kitmanagement", "Manage the kit system.", Rank.HELPER, new String[] {"kitman", "kman"});

        addSubCommand(new KitManagementCreateCommand());
        addSubCommand(new KitManagementAddCommand());
        addSubCommand(new KitManagementEditCommand());
    }

}
