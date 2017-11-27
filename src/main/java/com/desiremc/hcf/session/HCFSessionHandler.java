package com.desiremc.hcf.session;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.RedBlackTree;
import com.desiremc.hcf.listener.ConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class HCFSessionHandler extends BasicDAO<HCFSession, Integer>
{

    private static final boolean DEBUG = false;
    
    private static HCFSession console;

    private static HCFSessionHandler instance;

    private RedBlackTree<UUID, HCFSession> sessions;

    private static int nextId = 0;

    public HCFSessionHandler()
    {
        super(HCFSession.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(HCFSession.class);

        sessions = new RedBlackTree<>();
        console = new HCFSession();
        if (count() > 0)
        {
            nextId = findOne(createQuery().order("-_id")).getId() + 1;
        }
    }

    public static int getNextId()
    {
        return nextId++;
    }

    public static void initialize()
    {
        instance = new HCFSessionHandler();
    }

    public static Collection<HCFSession> getSessions()
    {
        return Collections.unmodifiableCollection(getInstance().sessions.values());
    }

    public static HCFSession getHCFSession(UUID uuid)
    {
        return getHCFSession(uuid, DesireCore.getCurrentServer());
    }

    /**
     * Gets the session of a user and initializes it if it does not yet exist.
     * 
     * @param o
     * @return
     */
    public static HCFSession getHCFSession(UUID uuid, String server)
    {
        if (DEBUG)
        {
            System.out.println("getHCFSession(UUID) called with " + (uuid == null ? "null" : uuid.toString()) + ".");
        }
        if (uuid == null)
        {
            return null;
        }
        HCFSession session = getInstance().sessions.get(uuid);
        if (session != null)
        {
            if (DEBUG)
            {
                System.out.println("getHCFSession(UUID) found a logged in user.");
            }
            return session;
        }
        else
        {
            if (DEBUG)
            {
                System.out.println("getHCFSession(UUID) no user found.");
            }
            return initializeHCFSession(uuid, server, false);
        }

    }

    public static HCFSession getHCFSession(CommandSender sender)
    {
        return getHCFSession(sender, DesireCore.getCurrentServer());
    }

    public static HCFSession getHCFSession(CommandSender sender, String server)
    {
        if (sender instanceof Player)
        {
            return getHCFSession(((Player) sender).getUniqueId(), server);
        }
        else if (sender instanceof ConsoleCommandSender)
        {
            return console;
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static HCFSession getHCFSessionByName(String name, String server)
    {
        OfflinePlayer p = Bukkit.getPlayerExact(name);
        if (p == null)
        {
            p = Bukkit.getOfflinePlayer(name);
        }
        if (p == null)
        {
            return null;
        }
        if (p.isOnline())
        {
            return getHCFSession(p.getUniqueId(), server);
        }
        else
        {
            return initializeHCFSession(p.getUniqueId(), server, false);
        }
    }

    public static HCFSession initializeHCFSession(UUID uuid, boolean cache)
    {
        return initializeHCFSession(uuid, DesireCore.getCurrentServer(), cache);
    }

    public static HCFSession initializeHCFSession(UUID uuid, String server, boolean cache)
    {
        if (DEBUG)
        {
            System.out.println("initializeHCFSession(UUID, boolean) called with values " + uuid.toString() + " and " + cache + ".");
        }
        Query<HCFSession> query = getInstance().createQuery();
        query.field("uuid").equal(uuid);
        query.field("server").equal(server);
        HCFSession session = getInstance().findOne(query);
        if (session == null)
        {
            if (DEBUG)
            {
                System.out.println("initializeHCFSession(UUID, boolean) HCFSession not found.");
            }
            session = createHCFSession(uuid, server);
        }
        if (DEBUG)
        {
            System.out.println("initializeHCFSession(UUID, boolean) setting base session.");
        }
        session.setSession(SessionHandler.getSession(uuid));
        if (cache)
        {
            if (DEBUG)
            {
                System.out.println("initializeHCFSession(UUID, boolean) caching HCFSession.");
            }
            getInstance().sessions.put(uuid, session);
        }
        return session;
    }

    public static boolean endSession(HCFSession s)
    {
        getInstance().save(s);
        getInstance().sessions.delete(s.getUniqueId());

        // TODO change this over
        return true;
    }

    public static HCFSession findOfflinePlayerByName(String name)
    {
        Session session = SessionHandler.getInstance().findOne("name", name);
        if (session == null)
        {
            return null;
        }
        HCFSession hcfSession = instance.findOne("uuid", session.getUniqueId());
        hcfSession.setSession(session);

        return hcfSession;
    }

    private static HCFSession createHCFSession(UUID uuid, String server)
    {
        HCFSession session = new HCFSession();
        session.assignDefault(uuid, server);
        getInstance().save(session);

        ConnectionListener.firstJoin.add(uuid);

        return session;
    }

    public static HCFSessionHandler getInstance()
    {
        if (instance == null)
        {
            initialize();
        }
        return instance;
    }

    @Override
    public Key<HCFSession> save(HCFSession entity)
    {
        return super.save(entity);
    }

}
