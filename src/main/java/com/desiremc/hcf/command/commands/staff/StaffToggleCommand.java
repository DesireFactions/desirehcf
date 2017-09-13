package com.desiremc.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.StaffAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

public class StaffToggleCommand extends ValidCommand {

    public StaffToggleCommand() {
        super("toggle", "toggle staff mode", Rank.ADMIN, new String[] {}, "mode");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.toggleStaffMode((Player) sender);
    }

}
