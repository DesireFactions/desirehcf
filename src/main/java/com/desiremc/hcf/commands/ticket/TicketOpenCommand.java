package com.desiremc.hcf.commands.ticket;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.tickets.TicketHandler;

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
