package com.desiremc.hcf.command.commands.ticket;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.IntegerParser;
import com.desiremc.hcf.parser.StringParser;
import com.desiremc.hcf.session.Rank;

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
