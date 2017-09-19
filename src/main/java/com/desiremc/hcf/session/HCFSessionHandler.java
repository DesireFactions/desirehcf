package com.desiremc.hcf.session;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;

public class HCFSessionHandler extends BasicDAO<HCFSession, UUID>
{

    private static HCFSession console;

    private static HCFSessionHandler instance;

    private List<HCFSession> sessions;

    public HCFSessionHandler()
    {
        super(HCFSession.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        sessions = new LinkedList<>();
        console = new HCFSession();
    }

    public static void initialize()
    {
        instance = new HCFSessionHandler();
    }

    public static List<HCFSession> getSessions()
    {
        return instance.sessions;
    }

    /**
     * Gets the session of a user and initializes it if it does not yet exist.
     * 
     * @param o
     * @return
     */
    public static HCFSession getHCFSession(Object o)
    {
        HCFSession session = null;
        if (o instanceof OfflinePlayer || o instanceof UUID)
        {
            for (HCFSession s : instance.sessions)
            {
                if (s.getUniqueId().equals(o instanceof OfflinePlayer ? ((OfflinePlayer) o).getUniqueId() : o)) { return s; }
            }
            session = initializeHCFSession(o, false);
        }
        else if (o instanceof String)
        {
            String name = (String) o;
            for (HCFSession s : instance.sessions)
            {
                if (s.getName().equalsIgnoreCase(name)) { return s; }
            }
            List<HCFSession> results = instance.createQuery().field("name").equal(name).asList();
            if (results.size() == 1) { return results.get(0); }
        }
        else if (o instanceof ConsoleCommandSender)
        {
            session = console;
        }
        return session;
    }

    public static HCFSession initializeHCFSession(Object o, boolean cache)
    {
        HCFSession session = instance.findOne("uuid", o instanceof OfflinePlayer ? ((OfflinePlayer) o).getUniqueId() : o);
        if (session == null)
        {
            session = createHCFSession(o);
        }
        if (cache)
        {
            instance.sessions.add(session);
        }
        return session;
    }

    public static boolean endSession(HCFSession s)
    {
        instance.save(s);
        return instance.sessions.remove(s);
    }

    private static HCFSession createHCFSession(Object o)
    {
        OfflinePlayer op;
        if (o instanceof OfflinePlayer)
        {
            op = (OfflinePlayer) o;
        }
        else if (o instanceof UUID)
        {
            op = Bukkit.getOfflinePlayer((UUID) o);
        }
        else
        {
            return null;
        }
        if (op == null) { return null; }

        HCFSession session = new HCFSession();
        session.setUniqueId(op.getUniqueId());
        session.setLives(0);
        session.setKills(new HashMap<>());
        session.setSafeTimeLeft(DesireCore.getConfigHandler().getInteger("timers.pvp.time"));
        session.setSettings(new HashMap<>());

        instance.save(session);

        return session;
    }

    public static HCFSessionHandler getInstance()
    {
        return instance;
    }

}
