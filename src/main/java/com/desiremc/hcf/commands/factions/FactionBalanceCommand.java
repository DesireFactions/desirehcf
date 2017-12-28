package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

import java.util.List;

public class FactionBalanceCommand extends FactionValidCommand
{

    public FactionBalanceCommand()
    {
        super("balance", "Show your faction balance", true, new String[] {"money", "bal"});

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .setOptional()
                .setAllowsConsole()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (!arguments.get(0).hasValue() && !sender.hasFaction())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.no_faction", true, false);
            return;
        }

        Faction faction = arguments.get(0).hasValue() ? (Faction) arguments.get(0).getValue() : sender.getFaction();

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.balance", true, false, "{faction}", faction.getName(), "{balance}", StringUtils.doubleFormat(faction.getBalance()));
    }
}
