package com.desiremc.hcf.commands.lives;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerHCFSessionParser;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.validator.PlayerHasDeathbanValidator;
import com.desiremc.hcf.validator.PlayerHasLivesValidator;

public class LivesUseCommand extends ValidCommand
{

    public LivesUseCommand()
    {
        super("use", "Use a life to revive another player.", Rank.GUEST, new String[] { "player" });
        addParser(new PlayerHCFSessionParser(), "player");
        addValidator(new PlayerHasDeathbanValidator(), "player");
        addValidator(new PlayerHasLivesValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(sender);
        HCFSession target = (HCFSession) args[0];

        target.revive(DesireCore.getCurrentServer());
        
        if (!session.getRank().isManager())
        {
            session.takeLives(1);
        }
    }

}
