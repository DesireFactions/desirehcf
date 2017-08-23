package me.borawski.hcf.command.commands.ticket;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class TicketCommand extends ValidBaseCommand {

    public TicketCommand() {
        super("ticket", "Manages the ticket system.", Rank.GUEST, "tickets", "tick", "pe", "petition");
        addSubCommand(new TicketOpenCommand());
    }

}
