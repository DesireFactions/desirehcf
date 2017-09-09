package com.desiremc.hcf.session;

import java.util.List;

import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.hcf.Core;

public class FactionSessionHandler extends BasicDAO<FactionSession, String> {

    private static FactionSessionHandler instance;

    private static List<FactionSession> cache;

    public FactionSessionHandler() {
        super(FactionSession.class, Core.getInstance().getMongoWrapper().getDatastore());

        cache = find().asList();
    }
    
    public static void initialize() {
        instance = new FactionSessionHandler();
    }

    public static FactionSession getFactionSession(String name) {
        for (FactionSession fs : cache) {
            if (fs.getName().equalsIgnoreCase(name)) {
                return fs;
            }
        }
        return null;
    }

    public static FactionSessionHandler getInstance() {
        return instance;
    }

}
