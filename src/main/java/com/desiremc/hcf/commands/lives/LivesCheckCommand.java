package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.PlayerHCFSessionParser;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesCheckCommand extends ValidCommand
{

    public LivesCheckCommand()
    {
        super("check", "Check how many lives you have.", Rank.GUEST, ARITY_OPTIONAL, new String[] {"target"});

        addParser(new PlayerHCFSessionParser(), "target");

        addOptionalNonexistantValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession session;
        if (args.length == 0)
        {
            session = HCFSessionHandler.getHCFSession(((Player) sender).getUniqueId());
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.check.self",
                    "{lives}", session.getLives());
        }
        else
        {
            session = (HCFSession) args[0];
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.check.others",
                    "{lives}", session.getLives(),
                    "{target}", session.getName());
        }

    }

}
