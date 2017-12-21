package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.DoubleParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.faction.Faction;

import java.util.List;

public class FactionDTRCommand extends ValidCommand
{

    protected FactionDTRCommand()
    {
        super("dtr", "Sets the DTR of a faction.", Rank.SRMOD);
        
        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .build());
        
        addArgument(CommandArgumentBuilder.createBuilder(Double.class)
                .setName("dtr")
                .setParser(new DoubleParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = (Faction) arguments.get(0).getValue();
        double dtr = (Double) arguments.get(1).getValue();
        
        faction.setDTR(dtr);
        faction.save();
        
        DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.dtr");
    }

}
