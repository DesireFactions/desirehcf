package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;
import com.desiremc.hcf.validators.SenderFactionLeaderValidator;
import com.desiremc.hcf.validators.SenderFactionSizeValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.SenderNotTargetValidator;
import com.desiremc.hcf.validators.TargetSameFactionValidator;

import java.util.List;

public class FactionLeaderCommand extends FactionValidCommand
{

    protected FactionLeaderCommand()
    {
        super("leader", "Sets a player as the new leader a faction.", true);

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addSenderValidator(new SenderHasFactionValidator())
                .addSenderValidator(new SenderFactionLeaderValidator())
                .addSenderValidator(new SenderFactionSizeValidator(2))
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new TargetSameFactionValidator())
                .build());

    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        FSession target = (FSession) arguments.get(0).getValue();

        target.setFactionRank(FactionRank.LEADER);
        sender.setFactionRank(FactionRank.OFFICER);

        target.save();
        sender.save();

        String message = DesireHCF.getLangHandler().renderMessage("factions.new_leader",
                "{old}", sender.getName(),
                "{new}", target.getName());
        
        for (FSession member : sender.getFaction().getMembers())
        {
            member.sendMessage(message);
        }
    }

}
