package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.EnderchestHandler;

import java.util.List;

public class EnderChestCommand extends ValidCommand
{

    public EnderChestCommand()
    {
        super("enderchest", "Toggle the ender chest.", Rank.ADMIN, true, new String[] {"chest", "ender"});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        if (EnderchestHandler.getEnderChestStatus())
        {
            EnderchestHandler.setEnderchestStatus(false);
            DesireHCF.getLangHandler().sendRenderMessage(sender, "enderchest.enabled");
        }
        else
        {
            EnderchestHandler.setEnderchestStatus(true);
            DesireHCF.getLangHandler().sendRenderMessage(sender, "enderchest.disabled");
        }
    }
}