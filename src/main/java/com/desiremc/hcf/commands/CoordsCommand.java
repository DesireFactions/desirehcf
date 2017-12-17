package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

import java.util.List;

public class CoordsCommand extends ValidCommand
{

    public CoordsCommand()
    {
        super("coords", "List of all important coordinates.", Rank.GUEST, new String[] {"coordinates"});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        for (String str : DesireHCF.getLangHandler().getStringList("coords"))
        {
            sender.sendMessage(str);
        }
    }

}
