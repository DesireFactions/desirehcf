package me.borawski.hcf.command.commands.ticket;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.StringParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.tickets.TicketHandler;
import me.borawski.hcf.util.StringUtils;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class TicketOpenCommand extends ValidCommand {

    public TicketOpenCommand() {
        super("open", "Opens a new ticket.", Rank.GUEST, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "description" }, new String[] { "create", "new" });
        addParser(new StringParser(), "description");

        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        String text = StringUtils.compile(Arrays.copyOf(args, args.length, String[].class));

        TicketHandler.openTicket(sender, text);
        LANG.sendString(sender, "ticket.open");
    }

}
