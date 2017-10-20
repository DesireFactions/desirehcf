package com.desiremc.hcf.session;

import java.util.List;

import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;

public class FactionSessionHandler extends BasicDAO<FactionSession, String>
{

    private static FactionSessionHandler instance;

    private static List<FactionSession> cache;

    public FactionSessionHandler()
    {
        super(FactionSession.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(FactionSession.class);
        cache = find().asList();
    }

    public static void initialize()
    {
        instance = new FactionSessionHandler();
    }

    public static FactionSession getFactionSession(String name)
    {
        for (FactionSession fs : cache)
        {
            if (fs.getName().equalsIgnoreCase(name)) { return fs; }
        }
        return null;
    }

    public static FactionSessionHandler getInstance()
    {
        return instance;
    }

}
