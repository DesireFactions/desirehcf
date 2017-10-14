package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.handler.EnderchestHandler;

public class EnderChestCommand extends ValidCommand
{

    public EnderChestCommand()
    {
        super("enderchest", "Toggle the ender chest.", Rank.ADMIN, new String[] {}, "chest", "ender");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (EnderchestHandler.getEnderChestStatus())
        {
            EnderchestHandler.setEnderchestStatus(false);
            HCFCore.getLangHandler().sendRenderMessage(sender, "enderchest.disabled");
        }
        else
        {
            EnderchestHandler.setEnderchestStatus(true);
            HCFCore.getLangHandler().sendRenderMessage(sender, "enderchest.enabled");
        }
    }
}