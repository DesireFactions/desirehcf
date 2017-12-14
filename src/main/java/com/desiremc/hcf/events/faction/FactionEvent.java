package com.desiremc.hcf.events.faction;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.desiremc.hcf.session.faction.Faction;

public class FactionEvent extends Event
{

    private static HandlerList handlerList;

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

    @Override
    public HandlerList getHandlers()
    {
        return handlerList;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

}
