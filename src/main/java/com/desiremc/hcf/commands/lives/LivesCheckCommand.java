package com.desiremc.hcf.commands.lives;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;

public class LivesCheckCommand extends FactionValidCommand
{

    public LivesCheckCommand()
    {
        super("check", "Check how many lives you have.", Rank.GUEST, new String[] {"target"});

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .setRequiredRank(Rank.HELPER)
                .setOptional()
                .build());
    }

    @Override
    public void validFactionRun(FSession fSession, String label[], List<CommandArgument<?>> args)
    {
        if (!args.get(0).hasValue())
        {
            DesireHCF.getLangHandler().sendRenderMessage(fSession.getSender(), "lives.check.self",
                    "{lives}", fSession.getLives());
        }
        else
        {
            FSession target = (FSession) args.get(0).getValue();
            DesireHCF.getLangHandler().sendRenderMessage(fSession.getSender(), "lives.check.others",
                    "{lives}", target.getLives(),
                    "{target}", target.getName());
        }

    }

}
