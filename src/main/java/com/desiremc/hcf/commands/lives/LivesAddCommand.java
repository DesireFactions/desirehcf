package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;

import java.util.List;

public class LivesAddCommand extends ValidCommand
{

    public LivesAddCommand()
    {
        super("add", "add lives", Rank.MODERATOR, false, new String[] { "give" });

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

        target.addLives(amount);
        target.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.add", true, false, "{amount}", String.valueOf(amount), "{player}", target.getName());
        if (target.isOnline())
        {
            DesireHCF.getLangHandler().sendRenderMessage(target.getSender(), "lives.recieved", true, false, "{amount}", String.valueOf(amount));
        }
    }

}