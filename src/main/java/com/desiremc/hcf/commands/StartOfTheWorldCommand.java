package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.SOTWHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StartOfTheWorldCommand extends ValidCommand
{

    private List<UUID> counter;

    public StartOfTheWorldCommand()
    {
        super("startitup", "Triggers the start of the world.", Rank.DEVELOPER, true, new String[] {"sotw"});

        counter = new ArrayList<>();
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        if (SOTWHandler.getSOTW())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "sotw.already");
            return;
        }

        if (counter.contains(sender.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "sotw.confirmed");
            SOTWHandler.getInstance().startSOTWTimer();
            counter.remove(sender.getUniqueId());
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "sotw.first");
            counter.add(sender.getUniqueId());
        }
    }

}
