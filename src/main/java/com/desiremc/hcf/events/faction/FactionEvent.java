package com.desiremc.hcf.events.faction;

import org.bukkit.event.Event;

import com.desiremc.hcf.session.faction.Faction;

public abstract class FactionEvent extends Event
{

    /**
     * The faction involved with the event.
     */
    protected Faction faction;

    public FactionEvent(Faction faction)
    {
        this.faction = faction;
    }

    /**
     * @return the faction involved with this event.
     */
    public Faction getFaction()
    {
        return faction;
    }

}
