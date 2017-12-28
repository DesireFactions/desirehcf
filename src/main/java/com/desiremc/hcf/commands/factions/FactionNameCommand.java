package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.FactionNameNotTaken;
import com.desiremc.hcf.validators.SenderFactionLeaderValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

public class FactionNameCommand extends FactionValidCommand
{
    protected FactionNameCommand()
    {
        super("name", "Change your faction name.", Rank.GUEST, new String[] {"rename"});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionLeaderValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new FactionNameNotTaken())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        String name = (String) arguments.get(0).getValue();

        faction.setName(name);

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.name.name_set", true, false, "{name}", name, "{player}", sender.getName()));
    }
}
