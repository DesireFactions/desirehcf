package me.borawski.hcf.command.commands.staff;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class StaffCommand extends ValidBaseCommand {

    public StaffCommand() {
        super("staff", "staff tools", Rank.ADMIN);
        addSubCommand(new StaffToggleCommand());
        addSubCommand(new StaffFreezeCommand());
        addSubCommand(new StaffClicksPerSecondCommand());
    }

}
