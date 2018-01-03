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
import com.desiremc.hcf.validators.PlayerHasFactionInviteValidator;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

public class FactionDeinviteCommand extends FactionValidCommand
{

    public FactionDeinviteCommand()
    {
        super("deinvite", "Deinvite a player from your faction.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new PlayerHasFactionInviteValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        FSession target = (FSession) arguments.get(0).getValue();

        faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.invite.remove", false, false, "{player}", sender.getName(), "{target}", target.getName()));
        faction.removeInvite(target);
        faction.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.invite.remove", true, false, "{player}", sender.getName(), "{target}", target.getName()));

        if (target.isOnline())
        {
            DesireHCF.getLangHandler().sendRenderMessage(target.getSender(), "factions.invite.remove_target", true, false, "{faction}", faction.getName());
        }
    }
}