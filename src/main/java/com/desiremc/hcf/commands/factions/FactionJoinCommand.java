package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderHasFactionInviteValidator;
import com.desiremc.hcf.validators.SenderHasNoFactionValidator;

import java.util.List;

public class FactionJoinCommand extends FactionValidCommand
{

    public FactionJoinCommand()
    {
        super("join", "Join a faction.", true, new String[] {});

        addSenderValidator(new SenderHasNoFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .addValidator(new SenderHasFactionInviteValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = (Faction) arguments.get(0).getValue();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.join.all", "{player}", sender.getName()));

        faction.removeInvite(sender);
        faction.addMember(sender);

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.join.sender", "{faction}", faction.getName());
    }
}
