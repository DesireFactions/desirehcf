package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

import java.util.List;

public class LivesCheckCommand extends ValidCommand
{

    public LivesCheckCommand()
    {
        super("check", "Check how many lives you have.", Rank.GUEST, new String[] {"target"});

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        FSession session;
        if (args.size() == 0)
        {
            session = FSessionHandler.getFSession((sender.getUniqueId()));
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.check.self",
                    "{lives}", session.getLives());
        }
        else
        {
            session = (FSession) args.get(0).getValue();
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.check.others",
                    "{lives}", session.getLives(),
                    "{target}", session.getName());
        }

    }

}
