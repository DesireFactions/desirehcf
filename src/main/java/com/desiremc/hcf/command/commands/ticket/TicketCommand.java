package com.desiremc.hcf.command.commands.ticket;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class TicketCommand extends ValidBaseCommand {

    public TicketCommand() {
        super("ticket", "Manages the ticket system.", Rank.GUEST, "tickets", "tick", "pe", "petition");
        addSubCommand(new TicketOpenCommand());
    }

}
