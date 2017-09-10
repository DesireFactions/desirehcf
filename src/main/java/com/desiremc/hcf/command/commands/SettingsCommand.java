package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.old_gui.PlayerSettingsGUI;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerSenderValidator;

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
