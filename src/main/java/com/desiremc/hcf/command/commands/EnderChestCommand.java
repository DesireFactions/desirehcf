package com.desiremc.hcf.command.commands;

import com.desiremc.hcf.handler.EnderchestHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;

public class EnderChestCommand extends ValidCommand
{

    public EnderChestCommand()
    {
        super("enderchest", "Toggle the ender chest.", Rank.ADMIN, new String[]{}, "chest", "ender");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        if (EnderchestHandler.getEnderChestStatus())
        {
            EnderchestHandler.setEnderchestStatus(false);
            DesireCore.getLangHandler().sendRenderMessage(sender, "enderchest.disabled");
        } else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "enderchest.enabled");
            EnderchestHandler.setEnderchestStatus(true);
        }
    }
}