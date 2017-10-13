package com.desiremc.hcf.commands;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.api.LangHandler;
import com.desiremc.hcf.handler.EnderchestHandler;
import org.bukkit.command.CommandSender;

public class EnderChestCommand extends ValidCommand
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

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
            LANG.sendRenderMessage(sender, "enderchest.disabled");
        } else
        {
            LANG.sendRenderMessage(sender, "enderchest.enabled");
            EnderchestHandler.setEnderchestStatus(true);
        }
    }
}