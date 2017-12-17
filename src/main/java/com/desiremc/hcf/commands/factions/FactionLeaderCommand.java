package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.HCFSessionParser;
import com.desiremc.hcf.session.HCFSession;
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
