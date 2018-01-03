package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

public class FactionLogsCommand extends FactionValidCommand
{

    public FactionLogsCommand()
    {
        super("logs", "View the edges of the nearby factions.", new String[] {"log"});

        addSenderValidator(new SenderHasFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("page")
                .setParser(new IntegerParser())
                .setOptional()
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .setOptional()
                .setRequiredRank(Rank.HELPER)
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction;
        int page = arguments.get(0).hasValue() ? ((Number) arguments.get(0).getValue()).intValue() : 0;

        if (!arguments.get(1).hasValue())
        {
            faction = sender.getFaction();
        }
        else
        {
            faction = (Faction) arguments.get(1).getValue();
        }

        FactionHandler.sendLogList(sender, faction, page);
    }
}
