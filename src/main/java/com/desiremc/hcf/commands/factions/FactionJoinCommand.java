package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionRank;
import com.desiremc.hcf.validators.SenderHasFactionInviteValidator;
import com.desiremc.hcf.validators.SenderHasNoFactionValidator;

public class FactionJoinCommand extends FactionValidCommand
{

    public FactionJoinCommand()
    {
        super("join", "Join a faction.", true, new String[] {});

        addSenderValidator(new SenderHasNoFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .addSenderValidator(new SenderHasNoFactionValidator())
                .addValidator(new SenderHasFactionInviteValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = (Faction) arguments.get(0).getValue();

        if (faction.isNormal())
        {
            faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.join.all", true, false, "{player}", sender.getName()));
            faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.join.all", true, false, "{player}", sender.getName()));
            faction.removeInvite(sender);
        }

        faction.addMember(sender);
        faction.save();

        sender.setFaction(faction);
        if (faction.isNormal())
        {
            sender.setFactionRank(FactionRank.MEMBER);
        }
        else
        {
            sender.setFactionRank(FactionRank.COLEADER);
        }
        sender.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.join.sender", true, false, "{faction}", faction.getName());
    }
}
