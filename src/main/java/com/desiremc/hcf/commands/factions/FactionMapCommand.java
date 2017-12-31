package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;

public class FactionMapCommand extends FactionValidCommand
{

    public FactionMapCommand()
    {
        super("map", "View the edges of the nearby factions.");
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        // TODO Auto-generated method stub
        
    }

}
