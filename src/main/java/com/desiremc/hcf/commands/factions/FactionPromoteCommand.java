package com.desiremc.hcf.commands.factions;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionRank;
import com.desiremc.hcf.validators.SenderCanPromoteTargetValidator;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderFactionSizeValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.SenderNotTargetValidator;
import com.desiremc.hcf.validators.TargetSameFactionValidator;

public class FactionPromoteCommand extends FactionValidCommand
{
    protected FactionPromoteCommand()
    {
        super("promote", "Promote a player in your faction.", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());
        addSenderValidator(new SenderFactionSizeValidator(2));

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new TargetSameFactionValidator())
                .addValidator(new SenderCanPromoteTargetValidator())
                .build());

    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        FSession target = (FSession) arguments.get(0).getValue();
        Faction faction = sender.getFaction();
        FactionRank rank = FactionRank.getNextRank(target.getFactionRank());

        target.setFactionRank(rank);

        faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.promote.valid", false, false, "{player}", sender.getName(), "{target}", target.getName(),
                "{rank}", StringUtils.capitalize(rank.name().replace("_", " ").toLowerCase())));
        faction.save();

        target.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.promote.valid", true, false, "{player}", sender.getName(), "{target}", target.getName(),
                "{rank}", StringUtils.capitalize(rank.name().replace("_", " ").toLowerCase())));
    }
}
