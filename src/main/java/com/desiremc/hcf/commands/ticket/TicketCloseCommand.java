package com.desiremc.hcf.commands.ticket;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;

public class TicketCloseCommand extends ValidCommand {

    public TicketCloseCommand() {
        super("close", "Close a ticket with a comment.", Rank.MODERATOR, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "ticket", "response" }, new String[] {});
        addParser(new IntegerParser(), "ticket");
        addParser(new StringParser(), "response");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {

    }

}
