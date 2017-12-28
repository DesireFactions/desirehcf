package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.FactionNotNeutralValidator;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

public class FactionNeutralCommand extends FactionValidCommand
{
    protected FactionNeutralCommand()
    {
        super("neutral", "Neutral another faction", Rank.GUEST);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .addValidator(new FactionNotNeutralValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction target = (Faction) arguments.get(0).getValue();
        Faction faction = sender.getFaction();

        target.removeAlly(faction);
        faction.removeAlly(target);

        target.broadcast(DesireHCF.getLangHandler().renderMessage("factions.neutral.sent", true, false, "{faction}", faction.getName()));
        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.neutral.sent", true, false, "{faction}", target.getName()));
    }
}
