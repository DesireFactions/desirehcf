package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.PlayerHCFSessionParser;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.validator.PlayerHasDeathbanValidator;
import org.bukkit.command.CommandSender;

public class ReviveCommand extends ValidCommand
{

    public ReviveCommand()
    {
        super("revive", "Revive a player before their ban.", Rank.HELPER, ARITY_REQUIRED_VARIADIC, new String[] {"target", "reason"});

        addParser(new PlayerHCFSessionParser(), "target");
        addParser(new StringParser(), "reason");

        addValidator(new PlayerHasDeathbanValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession target = (HCFSession) args[0];
        String reason = (String) args[1];

        target.revive(reason, true, PlayerUtils.getUUIDFromSender(sender));

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.revive", "{target}", target.getName());

    }

}
