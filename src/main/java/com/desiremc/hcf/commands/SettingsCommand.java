package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.old_gui.PlayerSettingsGUI;

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
        new PlayerSettingsGUI(HCFCore.getInstance(), (Player) sender).show();
    }
}
