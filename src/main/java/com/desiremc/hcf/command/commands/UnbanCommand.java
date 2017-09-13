package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.validator.PlayerSenderValidator;
import com.desiremc.hcf.validator.SenderOutranksValidator;

public class UnbanCommand extends ValidCommand
{

	public UnbanCommand()
	{
		super("unban", "Unban a user from the server.", Rank.MODERATOR, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[]{"target"});
		addParser(new PlayerSessionParser(), "target");
		addValidator(new PlayerSenderValidator());
		addValidator(new SenderOutranksValidator(), "target");
	}

	@Override
	public void validRun(CommandSender sender, String label, Object... args)
	{
		Session target = (Session) args[0];

		if (target.isBanned() != null && !target.isBanned().isRepealed())
		{
			LANG.sendRenderMessage(sender, "ban.unban_message",
					"{player}", target.getName());
			target.isBanned().setRepealed(true);
		}
		else
		{
			LANG.sendRenderMessage(sender, "ban.unban_invalid",
					"{player}", target.getName());
		}
	}

}
