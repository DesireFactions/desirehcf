package com.desiremc.hcf.session;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.listener.ConnectionListener;

public class FSessionHandler extends BasicDAO<FSession, Integer>
{

    private static FSession console;

    private static FSessionHandler instance;

    private static HashMap<UUID, FSession> sessions;
    private static HashMap<UUID, FSession> onlineSessions;

    private static int nextId = 0;

    public FSessionHandler()
    {
        super(FSession.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(FSession.class);

        sessions = new HashMap<>();
        onlineSessions = new HashMap<>();
        for (FSession fSession : find(createQuery().field("server").equal(DesireCore.getCurrentServer())))
        {
            fSession.setSession(SessionHandler.getGeneralSession(fSession.getUniqueId()));
            sessions.put(fSession.getUniqueId(), fSession);
            if (fSession.isOnline())
            {
                onlineSessions.put(fSession.getUniqueId(), fSession);
            }
        }

        console = new FSession();
        if (count() > 0)
        {
            nextId = findOne(createQuery().order("-_id")).getId() + 1;
        }

        for (FSession fSession : find())
        {
            sessions.put(fSession.getUniqueId(), fSession);
        }
    }

    public static int getNextId()
    {
        return nextId++;
    }

    public static void initialize()
    {
        instance = new FSessionHandler();
    }

    /**
     * Gets a collection of all currently connected {@link FSession}. This collection is immutable, so do not edit it or
     * the operations will throw {@link UnsupportedOperationException UnsupportedOperationExceptions}
     * 
     * @return
     */
    public static Collection<FSession> getFSessions()
    {
        return Collections.unmodifiableCollection(sessions.values());
    }

    /**
     * Gets the {@link FSession} of a user referenced by their {@link UUID} for the given server. This will not save
     * them to the online player list.
     * 
     * @param uuid the {@link UUID} of the player.
     * @param server the server to search for.
     * @return the {@link FSession} if found.
     */
    public static FSession getGeneralFSession(UUID uuid)
    {
        if (uuid == null)
        {
            return null;
        }
        FSession session = sessions.get(uuid);
        if (session != null)
        {
            return session;
        }
        else
        {
            return initializeFSession(uuid);
        }

    }

    public static FSession getOnlineFSession(UUID uuid)
    {
        if (uuid == null)
        {
            return null;
        }
        return onlineSessions.get(uuid);
    }

    public static FSession getFSession(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getOnlineFSession(((Player) sender).getUniqueId());
        }
        else if (sender instanceof ConsoleCommandSender)
        {
            return console;
        }
        return null;
    }

    public static FSession getFSessionByName(String name)
    {
        for (FSession fSession : sessions.values())
        {
            if (fSession.getName().equals(name))
            {
                return fSession;
            }
        }
        return null;
    }

    public static FSession initializeFSession(UUID uuid)
    {
        FSession session = sessions.get(uuid);
        if (session == null)
        {
            session = createFSession(uuid);
        }
        onlineSessions.put(session.getUniqueId(), session);
        return session;
    }

    public static boolean endSession(FSession fSession)
    {
        fSession.save();
        onlineSessions.remove(fSession.getUniqueId());

        // TODO change this over
        return true;
    }

    private static FSession createFSession(UUID uuid)
    {
        FSession session = new FSession();
        session.assignDefaults(uuid, DesireCore.getCurrentServer());
        session.save();

        ConnectionListener.firstJoin.add(uuid);

        return session;
    }

    public static FSessionHandler getInstance()
    {
        if (instance == null)
        {
            initialize();
        }
        return instance;
    }

    @Override
    public Key<FSession> save(FSession entity)
    {
        return super.save(entity);
    }

}
