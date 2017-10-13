package com.desiremc.hcf.tickets;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.tickets.Ticket.Status;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

public class TicketHandler extends BasicDAO<Ticket, Integer> implements Runnable
{

    public static TicketHandler instance;

    private List<Ticket> tickets;

    private int openTickets;

    public TicketHandler()
    {
        super(Ticket.class, DesireCore.getInstance().getMongoWrapper().getDatastore());
        tickets = find(createQuery().where("status='OPEN'")).asList();
    }

    public static void openTicket(CommandSender sender, String text)
    {
        Ticket ticket = new Ticket(sender instanceof Player ? ((Player) sender).getUniqueId() : DesireCore.getConsoleUUID(), text);
        ticket.setId(instance.getNextId());
        instance.save(ticket);
        instance.tickets.add(ticket);
    }

    public static void closeTicket(CommandSender closer, Ticket ticket, String response)
    {
        ticket.setClosed(System.currentTimeMillis());
        ticket.setCloser(closer instanceof Player ? ((Player) closer).getUniqueId() : DesireCore.getConsoleUUID());
        ticket.setResponse(response);
        ticket.setStatus(Status.CLOSED);
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskLater(HCFCore.getInstance(), this, 3600);
        for (Session s : SessionHandler.getInstance().getSessions()) {
            if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
                HCFCore.getLangHandler().sendRenderMessage(s, "tickets.open", "{number}", String.valueOf(openTickets));
            }
        }
    }

    private int getNextId()
    {
        Ticket t = createQuery().order("-id").get();
        if (t != null)
        {
            return t.getId() + 1;
        }
        else
        {
            return 0;
        }
    }

    public static void initialize()
    {
        instance = new TicketHandler();
    }

    public static TicketHandler getInstance()
    {
        return instance;
    }

}
