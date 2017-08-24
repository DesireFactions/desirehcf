package me.borawski.hcf.command.commands.ticket;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.IntegerParser;
import me.borawski.hcf.parser.StringParser;
import me.borawski.hcf.session.Rank;

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
