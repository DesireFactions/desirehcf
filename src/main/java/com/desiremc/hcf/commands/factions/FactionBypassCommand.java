package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.newparsers.BooleanParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionHandler;

public class FactionBypassCommand extends FactionValidCommand
{

    public FactionBypassCommand()
    {
        super("bypass", "Toggle admin bypass mode on/off.", Rank.SRMOD, true, new String[] { "adminmode" });

        addArgument(CommandArgumentBuilder.createBuilder(Boolean.class)
                .setName("state")
                .setParser(new BooleanParser())
                .setOptional()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.bypass." + (FactionHandler.toggleBypassing(sender) ? "enable" : "disable"));
    }

}
