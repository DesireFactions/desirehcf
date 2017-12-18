package com.desiremc.hcf.commands.lives;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.PlayerHasDeathbanValidator;
import com.desiremc.hcf.validators.PlayerHasLivesValidator;

public class LivesUseCommand extends FactionValidCommand
{

    public LivesUseCommand()
    {
        super("use", "Use a life to revive another player.", Rank.GUEST, new String[] { "revive" });

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new PlayerHasDeathbanValidator())
                .addSenderValidator(new PlayerHasLivesValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String label[], List<CommandArgument<?>> args)
    {
        FSession target = (FSession) args.get(0).getValue();

        target.revive(sender.getUniqueId() + " used a life.", false, sender.getUniqueId());
        target.save();
        sender.takeLives(1);
        sender.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "lives.use", "{target}", target.getName());
    }

}
