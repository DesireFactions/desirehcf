package me.borawski.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.StaffAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerParser;
import me.borawski.hcf.session.Rank;

public class StaffFreezeCommand extends ValidCommand {

    public StaffFreezeCommand() {
        super("freeze", "freeze a target player", Rank.ADMIN, new String[] { "target" });
        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.freeze(sender, (Player) args[0]);
    }

}
