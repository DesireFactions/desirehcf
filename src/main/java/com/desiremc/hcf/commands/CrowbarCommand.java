package com.desiremc.hcf.commands;

import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.api.LangHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.listener.CrowbarHandler;

public class CrowbarCommand extends ValidCommand
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

    public CrowbarCommand()
    {
        super("crowbar", "Spawn in a new crowbar.", Rank.MODERATOR, new String[]{});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        ((Player) sender).getInventory().addItem(CrowbarHandler.getNewCrowbar());
        LANG.sendString(sender, "crowbar.new_crowbar");
    }
}