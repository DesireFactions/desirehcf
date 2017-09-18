package com.desiremc.hcf.commands.ticket;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class TicketCommand extends ValidBaseCommand {

    public TicketCommand() {
        super("ticket", "Manages the ticket system.", Rank.GUEST, "tickets", "tick", "pe", "petition");
        addSubCommand(new TicketOpenCommand());
    }

}
