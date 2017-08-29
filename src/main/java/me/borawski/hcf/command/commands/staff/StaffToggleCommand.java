package me.borawski.hcf.command.commands.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.StaffAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class StaffToggleCommand extends ValidCommand {

    public StaffToggleCommand() {
        super("toggle", "toggle staff mode", Rank.ADMIN, new String[] {}, "mode");
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        StaffAPI.toggleStaffMode((Player) sender);
    }

}
