package me.borawski.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.StaffAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class StaffMountCommand extends ValidCommand {

    public StaffMountCommand() {
        super("follow", "follow a player", Rank.ADMIN, new String[] { "target" }, "mount", "ride", "leash", "lead");
        addParser(new PlayerParser(), "target");
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.mount((Player) sender, (Player) args[0]);
    }

}
