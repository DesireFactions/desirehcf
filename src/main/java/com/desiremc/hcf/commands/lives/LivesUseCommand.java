package com.desiremc.hcf.commands.lives;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.PlayerHCFSessionParser;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.validator.PlayerHasDeathbanValidator;
import com.desiremc.hcf.validator.PlayerHasLivesValidator;

public class LivesUseCommand extends ValidCommand
{

    public LivesUseCommand()
    {
        super("use", "Use a life to revive another player.", Rank.GUEST, new String[] { "target" }, new String[] { "revive" });
        addParser(new PlayerHCFSessionParser(), "target");

        addValidator(new PlayerHasDeathbanValidator(), "target");
        addValidator(new PlayerHasLivesValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(sender);
        HCFSession target = (HCFSession) args[0];

        target.revive(session.getUniqueId() + " used a life.", false, session.getUniqueId());
        session.takeLives(1);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.use", "{target}", target.getName());
    }

}
