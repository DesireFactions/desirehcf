package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.handler.CrowbarHandler;
import com.desiremc.hcf.session.FSession;

import java.util.List;

public class CrowbarCommand extends FactionValidCommand
{

    public CrowbarCommand()
    {
        super("crowbar", "Spawn in a new crowbar.", Rank.GUEST, true, new String[]{});
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (sender.getBalance() < 15000)
        {

            DesireHCF.getLangHandler().sendRenderMessage(sender, "crowbar.not_enough_money", true, false);
            return;
        }

        sender.withdrawBalance(15000);
        sender.save();

        sender.getPlayer().getInventory().addItem(CrowbarHandler.getNewCrowbar());

        DesireHCF.getLangHandler().sendRenderMessage(sender, "crowbar.new_crowbar", true, false);
    }
}