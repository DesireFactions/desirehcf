package me.borawski.hcf.session;

import java.util.UUID;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "faction_players", noClassnameStored = true)
public class FactionPlayerSession {

    @Id
    private UUID uuid;

    private FactionSession faction;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public FactionSession getFaction() {
        return faction;
    }

    public void setFaction(FactionSession faction) {
        this.faction = faction;
    }

}
