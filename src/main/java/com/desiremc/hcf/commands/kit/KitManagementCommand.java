package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class KitManagementCommand extends ValidBaseCommand
{

    public KitManagementCommand()
    {
        super("kitmanagement", "Manage the kit system.", Rank.JRMOD, new String[] { "kitman", "kman" });

        addSubCommand(new KitManagementCreateCommand());
        addSubCommand(new KitManagementAddCommand());
        addSubCommand(new KitManagementEditCommand());
    }

}
