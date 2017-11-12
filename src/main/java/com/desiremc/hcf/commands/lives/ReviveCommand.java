package com.desiremc.hcf.commands.lives;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerHCFSessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.validator.PlayerHasDeathbanValidator;

public class ReviveCommand extends ValidCommand
{

    public ReviveCommand()
    {
        super("revive", "Revive a player before their ban.", Rank.JRMOD, ARITY_REQUIRED_VARIADIC, new String[] { "target", "reason" });

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
