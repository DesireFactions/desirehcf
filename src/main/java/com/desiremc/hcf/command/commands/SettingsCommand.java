package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.old_gui.PlayerSettingsGUI;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

public class SettingsCommand extends ValidCommand
{

    public SettingsCommand()
    {
        super("settings", "Change your VIP settings.", Rank.VIP, new String[]{});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        new PlayerSettingsGUI(DesireCore.getInstance(), (Player) sender).show();
    }
}
