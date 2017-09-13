package com.desiremc.hcf.command.commands.ticket;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.StringParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.tickets.TicketHandler;
import com.desiremc.hcf.util.StringUtils;
import com.desiremc.hcf.validator.PlayerValidator;

public class TicketOpenCommand extends ValidCommand {

    public TicketOpenCommand() {
        super("open", "Opens a new ticket.", Rank.GUEST, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "description" }, new String[] { "create", "new" });
        addParser(new StringParser(), "description");

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        String text = StringUtils.compile(Arrays.copyOf(args, args.length, String[].class));

        TicketHandler.openTicket(sender, text);
        LANG.sendString(sender, "ticket.open");
    }

}
