package com.desiremc.hcf.command.commands.staff;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class StaffCommand extends ValidBaseCommand {

    public StaffCommand() {
        super("staff", "staff tools", Rank.ADMIN);
        addSubCommand(new StaffToggleCommand());
        addSubCommand(new StaffFreezeCommand());
        addSubCommand(new StaffClicksPerSecondCommand());
        addSubCommand(new StaffInvisibilityCommand());
        addSubCommand(new StaffMountCommand());
    }

}