package com.desiremc.hcf.commands.lives;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;

public class LivesRemoveCommand extends ValidCommand
{

    public LivesRemoveCommand()
    {
        super("remove", "Remove lives from a player.", Rank.MODERATOR, new String[] { "take" });

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new PositiveIntegerParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        FSession target = (FSession) args.get(0).getValue();
        int amount = (Integer) args.get(1).getValue();

        target.takeLives(amount);
        target.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.remove",
                "{amount}", String.valueOf(amount),
                "{player}", target.getName());
    }

}