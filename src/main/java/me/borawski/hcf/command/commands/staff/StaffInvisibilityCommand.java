package me.borawski.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.StaffAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerParser;
import me.borawski.hcf.session.Rank;

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
