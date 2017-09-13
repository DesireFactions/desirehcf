package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.listener.CrowbarHandler;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

public class CrowbarCommand extends ValidCommand
{

    public CrowbarCommand()
    {
        super("crowbar", "Spawn in a new crowbar.", Rank.MODERATOR, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        ((Player) sender).getInventory().addItem(CrowbarHandler.getNewCrowbar());
        LANG.sendString(sender, "crowbar.new_crowbar");
    }
}