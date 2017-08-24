package me.borawski.hcf.session;

import java.util.HashMap;
import java.util.UUID;

import org.mongodb.morphia.dao.BasicDAO;

import me.borawski.hcf.Core;

public class FactionPlayerSessionHandler extends BasicDAO<FactionPlayerSession, Integer> {

    private HashMap<UUID, FactionPlayerSession> factionPlayerSessions;

    private static FactionPlayerSessionHandler instance;

    public FactionPlayerSessionHandler() {
        super(FactionPlayerSession.class, Core.getInstance().getMongoWrapper().getDatastore());

        factionPlayerSessions = new HashMap<UUID, FactionPlayerSession>();
    }

    public static void initialize() {
        instance = new FactionPlayerSessionHandler();
    }

    public HashMap<UUID, FactionPlayerSession> getFactionPlayerSessions() {
        return factionPlayerSessions;
    }

    public static FactionPlayerSessionHandler getInstance() {
        return instance;
    }

    public FactionPlayerSession getFactionPlayerSession(UUID id) {
        return factionPlayerSessions.get(id);
    }

    public void remove(String name) {
        factionPlayerSessions.remove(name);
    }

}
