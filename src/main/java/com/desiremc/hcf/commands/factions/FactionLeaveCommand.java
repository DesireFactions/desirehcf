package com.desiremc.hcf.commands.factions;

import java.util.List;

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

        faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.leave.all", false, false, "{player}", sender.getName()));
        faction.removeMember(sender);
        faction.save();

        sender.setFactionRank(null);
        sender.setFaction(FactionHandler.getWilderness());
        sender.setChannel(FactionChannel.GENERAL);
        sender.save();

        if (sender.hasClaimSession())
        {
            sender.clearClaimSession();
        }

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.leave.all", true, false, "{player}", sender.getName()));

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.leave.sender", true, false, "{faction}", faction.getName());
    }
}
