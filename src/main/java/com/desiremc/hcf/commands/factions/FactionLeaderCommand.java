package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.newparsers.HCFSessionParser;
import com.desiremc.hcf.newvalidators.SenderFactionLeaderValidator;
import com.desiremc.hcf.newvalidators.SenderFactionSizeValidator;
import com.desiremc.hcf.newvalidators.SenderHasFactionValidator;
import com.desiremc.hcf.newvalidators.SenderNotTargetValidator;
import com.desiremc.hcf.newvalidators.TargetSameFactionValidator;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class FactionLeaderCommand extends FactionValidCommand
{

    protected FactionLeaderCommand()
    {
        super("leader", "Sets a player as the new leader a faction.", true);

        addArgument(CommandArgumentBuilder.createBuilder(HCFSession.class)
                .setName("target")
                .setParser(new HCFSessionParser())
                .addSenderValidator(new SenderHasFactionValidator())
                .addSenderValidator(new SenderFactionLeaderValidator())
                .addSenderValidator(new SenderFactionSizeValidator(2))
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new TargetSameFactionValidator())
                .build());

    }

    @Override
    public void validFactionRun(HCFSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        HCFSession target = (HCFSession) arguments.get(0).getValue();

        target.setFactionRank(FactionRank.LEADER);
        sender.setFactionRank(FactionRank.OFFICER);

        target.save();
        sender.save();

        String message = DesireHCF.getLangHandler().renderMessage("factions.new_leader",
                "{old}", sender.getName(),
                "{new}", target.getName());
        
        for (HCFSession member : sender.getFaction().getMembers())
        {
            member.sendMessage(message);
        }
    }

}
