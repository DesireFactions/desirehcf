package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderFactionCanKickValidator;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.TargetSameFactionValidator;

public class FactionKickCommand extends FactionValidCommand
{

    public FactionKickCommand()
    {
        super("kick", "Kick a player from your faction.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new TargetSameFactionValidator())
                .addValidator(new SenderFactionCanKickValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        FSession target = (FSession) arguments.get(0).getValue();

        faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.kick.all", false, false, "{player}", sender.getName(), "{target}", target.getName()));
        faction.removeMember(target);
        faction.save();

        target.setFactionRank(null);
        target.setFaction(FactionHandler.getWilderness());
        target.save();


        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.kick.all", true, false, "{player}", sender.getName(), "{target}", target.getName()));
        DesireHCF.getLangHandler().sendRenderMessage(target.getSender(), "factions.kick.target", true, false, "{faction}", faction.getName(), "{player}", sender.getName());
    }
}
