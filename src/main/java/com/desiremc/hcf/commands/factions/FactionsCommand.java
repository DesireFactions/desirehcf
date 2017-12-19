package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class FactionsCommand extends ValidBaseCommand
{

    public FactionsCommand()
    {
        super("factions", "All factions commands.", new String[] {"f", "faction", "fact"});

        addSubCommand(new FactionAnnounceCommand());
        addSubCommand(new FactionBypassCommand());
        addSubCommand(new FactionClaimCommand());
        addSubCommand(new FactionCreateCommand());
        addSubCommand(new FactionLeaderCommand());
        addSubCommand(new FactionLocationCommand());
        addSubCommand(new FactionFocusCommand());
        //addSubCommand( new Faction_Command());
    }

}
