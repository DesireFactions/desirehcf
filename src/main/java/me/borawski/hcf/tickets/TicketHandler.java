package me.borawski.hcf.tickets;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.tickets.Ticket.Status;

public class TicketHandler extends BasicDAO<Ticket, Integer> implements Runnable {

    public static TicketHandler instance;

    private List<Ticket> tickets;

    private int openTickets;

    public TicketHandler() {
        super(Ticket.class, Core.getInstance().getMongoWrapper().getDatastore());
        tickets = find(createQuery().where("status='OPEN'")).asList();
    }

    public static void openTicket(CommandSender sender, String text) {
        Ticket ticket = new Ticket(sender instanceof Player ? ((Player) sender).getUniqueId() : Core.getConsoleUUID(), text);
        ticket.setId(instance.getNextId());
        instance.save(ticket);
        instance.tickets.add(ticket);
    }

    public static void closeTicket(CommandSender closer, Ticket ticket, String response) {
        ticket.setClosed(System.currentTimeMillis());
        ticket.setCloser(closer instanceof Player ? ((Player) closer).getUniqueId() : Core.getConsoleUUID());
        ticket.setResponse(response);
        ticket.setStatus(Status.CLOSED);
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), this, 3600);
        for (Session s : SessionHandler.getSessions()) {
            if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
                Core.getLangHandler().sendRenderMessage(s, "tickets.open", "{number}", String.valueOf(openTickets));
            }
        }
    }

    private int getNextId() {
        Ticket t = createQuery().order("-id").get();
        if (t != null) {
            return t.getId() + 1;
        } else {
            return 0;
        }
    }

    public static void initialize() {
        instance = new TicketHandler();
    }

    public static TicketHandler getInstance() {
        return instance;
    }

}
