package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.newvalidators.StringLengthValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.newvalidators.SenderHasNoFactionValidator;
import com.desiremc.hcf.session.HCFSession;

public class FactionCreateCommand extends FactionValidCommand
{

    public FactionCreateCommand()
    {
        super("create", "Crate a new faction.", true, new String[] { "new", "start" });

        addSenderValidator(new SenderHasNoFactionValidator());
        
        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new StringLengthValidator(DesireHCF.getConfigHandler().getInteger("factions.names.length.min"), DesireHCF.getConfigHandler().getInteger("factions.names.length.max")))
                .build());
    }

    @Override
    public void validFactionRun(HCFSession sender, String[] label, List<CommandArgument<?>> arguments)
    {

    }

}
