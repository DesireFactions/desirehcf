package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.gui.PlayerSettingsGUI;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

/**
 * Created by Ethan on 3/21/2017.
 */
public class SettingsCommand extends ValidCommand {

    public SettingsCommand() {
        super("settings", "Change your VIP settings.", Rank.VIP, new String[] {});
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        new PlayerSettingsGUI(Core.getInstance(), (Player) sender).show();
    }
}
