package com.desiremc.hcf.connection;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.api.FileHandler;
import com.desiremc.hcf.punishment.Punishment;
import com.desiremc.hcf.session.FactionSession;
import com.desiremc.hcf.session.Session;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoWrapper {

    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;

    public MongoWrapper() {
        FileHandler config = DesireCore.getConfigHandler();
        ServerAddress addr = new ServerAddress(config.getString("database.host"), config.getInteger("database.port"));

        List<MongoCredential> credentials = new ArrayList<>();
        credentials.add(MongoCredential.createCredential(config.getString("database.username"), config.getString("database.database"), config.getString("database.password").toCharArray()));

        mc = new MongoClient(addr, credentials);
        morphia = new Morphia();

        morphia.map(Session.class);
        morphia.map(Punishment.class);
        morphia.map(FactionSession.class);
        
        datastore = morphia.createDatastore(mc, config.getString("database.database"));
        datastore.ensureIndexes();
    }

    public MongoClient getMongoClient() {
        return mc;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public Datastore getDatastore() {
        return datastore;
    }

}
