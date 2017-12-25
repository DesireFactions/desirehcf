package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.TargetNoFactionValidator;
import com.desiremc.hcf.validators.TargetNoInviteValidator;
import com.desiremc.hcf.validators.TargetNotSameFactionValidator;

import java.util.List;

public class FactionInviteCommand extends FactionValidCommand
{

    public FactionInviteCommand()
    {
        super("invite", "Invite a player to your faction.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new TargetNotSameFactionValidator())
                .addValidator(new TargetNoFactionValidator())
                .addValidator(new TargetNoInviteValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        FSession target = (FSession) arguments.get(0).getValue();

        faction.addInvite(target);
        faction.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.invite.add", true, false, "{player}", sender.getName(), "{target}", target.getName()));

        if (target.isOnline())
        {
            DesireHCF.getLangHandler().sendRenderMessage(target.getSender(), "factions.invite.target", true, false, "{faction}", faction.getName());
        }
    }
}