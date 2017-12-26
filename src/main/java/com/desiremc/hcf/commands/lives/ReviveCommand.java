package com.desiremc.hcf.commands.lives;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.PlayerHasDeathbanValidator;

public class ReviveCommand extends ValidCommand
{

    public ReviveCommand()
    {
        super("revive", "Revive a player before their ban.", Rank.HELPER, false, new String[] {"target", "reason"});

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new PlayerHasDeathbanValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("reason")
                .setParser(new StringParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        FSession target = (FSession) args.get(0).getValue();
        String reason = (String) args.get(1).getValue();

        target.revive(reason, true, sender.getUniqueId());
        target.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.revive", true, false, "{target}", target.getName());

    }

}
