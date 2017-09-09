package com.desiremc.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.StaffAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerParser;
import com.desiremc.hcf.session.Rank;

public class StaffClicksPerSecondCommand extends ValidCommand {

    public StaffClicksPerSecondCommand() {
        super("cps", "starts clicks per second test on player", Rank.ADMIN, new String[] { "target" });
        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.clicksPerSecondTest(sender, (Player) args[0]);
    }

}