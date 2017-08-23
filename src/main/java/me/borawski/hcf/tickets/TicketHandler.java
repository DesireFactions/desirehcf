package me.borawski.hcf.tickets;

import org.mongodb.morphia.dao.BasicDAO;

import me.borawski.hcf.Core;

public class TicketHandler extends BasicDAO<Ticket, Integer> {

    public static TicketHandler instance;

    public TicketHandler() {
        super(Ticket.class, Core.getInstance().getMongoWrapper().getDatastore());
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
