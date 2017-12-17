package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.HCFSessionParser;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.validators.PlayerHasDeathbanValidator;
import com.desiremc.hcf.validators.PlayerHasLivesValidator;

import java.util.List;

public class LivesUseCommand extends ValidCommand
{

    public LivesUseCommand()
    {
        super("use", "Use a life to revive another player.", Rank.GUEST, new String[] {"revive"});

        addArgument(CommandArgumentBuilder.createBuilder(HCFSession.class)
                .setName("target")
                .setParser(new HCFSessionParser())
                .addValidator(new PlayerHasDeathbanValidator())
                .addSenderValidator(new PlayerHasLivesValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(sender.getUniqueId());
        HCFSession target = (HCFSession) args.get(0).getValue();

        target.revive(session.getUniqueId() + " used a life.", false, session.getUniqueId());
        session.takeLives(1);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.use", "{target}", target.getName());
    }

}
