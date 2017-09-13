package com.desiremc.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.StaffAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

public class StaffMountCommand extends ValidCommand {

    public StaffMountCommand() {
        super("follow", "follow a player", Rank.ADMIN, new String[] { "target" }, "mount", "ride", "leash", "lead");
        addParser(new PlayerParser(), "target");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.mount((Player) sender, (Player) args[0]);
    }

}
