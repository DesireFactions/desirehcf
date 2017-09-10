package com.desiremc.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.StaffAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerParser;
import com.desiremc.hcf.session.Rank;

public class StaffInvisibilityCommand extends ValidCommand {

    public StaffInvisibilityCommand() {
        super("invisibility", "toggle invisibility for player", Rank.ADMIN, new String[] { "target" });
        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.toggleInvisibility((Player) args[0]);
    }

}
