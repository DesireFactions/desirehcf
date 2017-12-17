package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.CrowbarHandler;

import java.util.List;

public class CrowbarCommand extends ValidCommand
{

    public CrowbarCommand()
    {
        super("crowbar", "Spawn in a new crowbar.", Rank.MODERATOR, true, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        sender.getPlayer().getInventory().addItem(CrowbarHandler.getNewCrowbar());

        DesireHCF.getLangHandler().sendRenderMessage(sender, "crowbar.new_crowbar");
    }
}