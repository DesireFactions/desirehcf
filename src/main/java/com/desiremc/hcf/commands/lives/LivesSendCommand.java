package com.desiremc.hcf.commands.lives;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.newparsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.PlayerHasEnoughLivesValidator;

public class LivesSendCommand extends FactionValidCommand
{
    public LivesSendCommand()
    {
        super("send", "send lives", Rank.GUEST, new String[] { "target", "amount" });

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new PositiveIntegerParser())
                .addValidator(new PlayerHasEnoughLivesValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String label[], List<CommandArgument<?>> args)
    {
        FSession target = (FSession) args.get(0).getValue();
        int amount = (Integer) args.get(1).getValue();

        sender.takeLives(amount);
        target.addLives(amount);
        sender.save();
        target.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "lives.send",
                "{amount}", String.valueOf(amount),
                "{player}", target.getName());

        if (target.isOnline())
        {
            DesireHCF.getLangHandler().sendRenderMessage(target.getSender(), "lives.send_target",
                    "{amount}", String.valueOf(amount),
                    "{player}", sender.getName());
        }
    }
}
