package com.desiremc.hcf.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.handler.SpawnHandler;

public class FSessionHandler extends BasicDAO<FSession, Integer>
{

    private static FSession console;

    private static FSessionHandler instance;

    private static HashMap<UUID, FSession> fSessions;
    private static HashMap<UUID, FSession> onlineFSessions;
    private static List<UUID> safeSessions;

    private static int nextId = 0;

    public FSessionHandler()
    {
        super(FSession.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(FSession.class);
    }

    public static int getNextId()
    {
        return ++nextId;
    }

    public static void initialize()
    {
        instance = new FSessionHandler();

        fSessions = new HashMap<>();
        onlineFSessions = new HashMap<>();
        safeSessions = new ArrayList<>();

        for (FSession fSession : instance.find(instance.createQuery().field("server").equal(DesireCore.getCurrentServer())))
        {
            fSession.setSession(SessionHandler.getGeneralSession(fSession.getUniqueId()));
            fSessions.put(fSession.getUniqueId(), fSession);
            if (nextId < fSession.getId())
            {
                nextId = fSession.getId();
            }
        }

        console = new FSession();
        console.setUniqueId(DesireCore.getConsoleUUID());
        console.setSession(SessionHandler.getConsoleSession());
    }

    /**
     * Gets a collection of all currently connected {@link FSession}. This collection is immutable, so do not edit it or
     * the operations will throw {@link UnsupportedOperationException UnsupportedOperationExceptions}
     *
     * @return collection of all FSessions.
     */
    public static Collection<FSession> getFSessions()
    {
        return Collections.unmodifiableCollection(fSessions.values());
    }

    /**
     * Gets the {@link FSession} of a user referenced by their {@link UUID} for the given server. This will not save
     * them to the online player list.
     *
     * @param uuid the {@link UUID} of the player.
     * @return the {@link FSession} if found.
     */
    public static FSession getGeneralFSession(UUID uuid)
    {
        if (uuid == null)
        {
            return null;
        }
        FSession fSession = fSessions.get(uuid);
        if (fSession != null)
        {
            return fSession;
        }
        else
        {
            return createFSession(uuid);
        }
    }

    public static FSession getOnlineFSession(UUID uuid)
    {
        if (uuid == null)
        {
            return null;
        }
        return onlineFSessions.get(uuid);
    }

    public static Collection<FSession> getOnlineFSessions()
    {
        return Collections.unmodifiableCollection(onlineFSessions.values());
    }

    public static List<UUID> getSafeOnlineFSessions()
    {
        return safeSessions;
    }

    public static FSession getConsoleFSession()
    {
        return console;
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
        for (FSession fSession : fSessions.values())
        {
            if (fSession.getName().equalsIgnoreCase(name))
            {
                return fSession;
            }
        }
        return null;
    }

    public static FSession initializeFSession(UUID uuid)
    {
        FSession fSession = fSessions.get(uuid);
        System.out.println(uuid.toString());
        if (fSession == null)
        {
            fSession = createFSession(uuid);
        }
        else
        {
            fSession.applyValues(instance.findOne(instance.createQuery()
                    .field("_id").equal(fSession.getId())
                    .field("server").equal(DesireCore.getCurrentServer())));
            fSession.save();
        }
        onlineFSessions.put(fSession.getUniqueId(), fSession);

        if (fSession.getSafeTimeLeft() > 0)
        {
            if (!safeSessions.contains(fSession.getUniqueId()))
            {
                safeSessions.add(fSession.getUniqueId());
            }
        }
        return fSession;
    }

    public static boolean endSession(FSession fSession)
    {
        fSession.save();
        onlineFSessions.remove(fSession.getUniqueId());
        safeSessions.remove(fSession.getUniqueId());
        // TODO change this over
        return true;
    }

    private static FSession createFSession(UUID uuid)
    {
        Session session = SessionHandler.getGeneralSession(uuid);
        FSession fSession = new FSession();
        fSession.assignDefaults(uuid, DesireCore.getCurrentServer());
        fSession.setSession(session);
        fSession.save();

        fSessions.put(uuid, fSession);

        SpawnHandler.getInstance().addPlayer(uuid);

        return fSession;
    }

    public static FSessionHandler getInstance()
    {
        if (instance == null)
        {
            initialize();
        }
        return instance;
    }
}
