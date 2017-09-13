package com.desiremc.hcf.command.commands;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.validator.PlayerIsBannedValidator;
import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;

public class UnbanCommand extends ValidCommand
{

    public UnbanCommand()
    {
        super("unban", "Unban a user from the server.", Rank.MODERATOR, new String[]{"target"});
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerIsBannedValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        LANG.sendRenderMessage(sender, "ban.unban_message",
                "{player}", target.getName());
        target.isBanned().setRepealed(true);
        Core.getPunishmentHandler().save(target.isBanned());
    }

}
