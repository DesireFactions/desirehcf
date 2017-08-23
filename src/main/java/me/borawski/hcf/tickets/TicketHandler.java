package me.borawski.hcf.tickets;

import java.util.List;

import org.bukkit.Bukkit;
import org.mongodb.morphia.dao.BasicDAO;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class TicketHandler extends BasicDAO<Ticket, Integer> implements Runnable {

    public static TicketHandler instance;

    private List<Ticket> openTickets;

    public TicketHandler() {
        super(Ticket.class, Core.getInstance().getMongoWrapper().getDatastore());
        openTickets = find().asList();
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), this, 3600);
        for (Session s : SessionHandler.getSessions()) {
            if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
                Core.getLangHandler().sendRenderMessage(s, "tickets.open", "{number}", String.valueOf(openTickets.size()));
            }
        }
    }

    public int getNext() {
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
