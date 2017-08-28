package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class StaffCommand extends ValidCommand {

    public StaffCommand() {
        super("staff", "gives you staff tools", Rank.ADMIN, new String[] {});
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Core.getStaffHandler().toggleStaffMode((Player) sender);
    }

}
