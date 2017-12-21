package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

import java.util.List;

public class FactionAnnounceCommand extends FactionValidCommand
{

    public FactionAnnounceCommand()
    {
        super("announce", "Announce something to all faction members.", true, new String[] { "ann" });

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("announcement")
                .setParser(new StringParser())
                .setVariableLength()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        String message = DesireHCF.getLangHandler().renderMessageNoPrefix("factions.announce",
                "{faction}", faction.getName(),
                "{message}", arguments.get(0),
                "{sender}", sender.getName());

        for (FSession online : faction.getOnlineMembers())
        {
            online.sendMessage(message);
        }
        for (FSession offline : faction.getOfflineMembers())
        {
            faction.addAnnouncement(offline, message);
        }
    }

}
