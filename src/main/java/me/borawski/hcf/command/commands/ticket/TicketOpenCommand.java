package me.borawski.hcf.command.commands.ticket;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.StringParser;
import me.borawski.hcf.session.Rank;

public class TicketOpenCommand extends ValidCommand {

    public TicketOpenCommand() {
        super("open", "Opens a new ticket.", Rank.GUEST, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "description" }, new String[] { "create", "new" });
        addParser(new StringParser(), "description");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        System.out.println(Arrays.toString(args));
    }

}
