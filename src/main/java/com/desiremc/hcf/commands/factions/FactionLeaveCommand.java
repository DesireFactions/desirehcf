package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionChannel;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderCanLeaveFactionValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

import java.util.List;

public class FactionLeaveCommand extends FactionValidCommand
{
    public FactionLeaveCommand()
    {
        super("leave", "Leave your faction.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderCanLeaveFactionValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();

        faction.removeMember(sender);

        sender.setFactionRank(null);
        sender.setFaction(FactionHandler.getWilderness());
        sender.setChannel(FactionChannel.GENERAL);

        sender.save();
        faction.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.leave.all", "{player}", sender.getName()));
        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.leave.sender", "{faction}", faction.getName());
    }
}
