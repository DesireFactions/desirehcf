package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionRank;
import com.desiremc.hcf.validators.SenderCanDemoteTargetValidator;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderFactionSizeValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.SenderNotTargetValidator;
import com.desiremc.hcf.validators.TargetSameFactionValidator;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class FactionDemoteCommand extends FactionValidCommand
{
    protected FactionDemoteCommand()
    {
        super("demote", "Demote a player in your faction.", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());
        addSenderValidator(new SenderFactionSizeValidator(2));

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new TargetSameFactionValidator())
                .addValidator(new SenderCanDemoteTargetValidator())
                .build());

    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        FSession target = (FSession) arguments.get(0).getValue();
        Faction faction = sender.getFaction();
        FactionRank rank = FactionRank.getLastRank(target.getFactionRank());

        target.setFactionRank(rank);

        target.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.demote.valid", "{player}", sender.getName(), "{target}", target.getName(),
                "{rank}", StringUtils.capitalize(rank.name().replace("_", " ").toLowerCase())));
    }
}
