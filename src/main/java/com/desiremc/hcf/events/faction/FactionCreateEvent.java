package com.desiremc.hcf.events.faction;

import org.bukkit.event.HandlerList;

import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class FactionCreateEvent extends FactionPlayerEvent
{

    private static HandlerList handerList = new HandlerList();

    /**
     * @param faction the {@link Faction} involved with the event.
     * @param fSession the {@link FSession} involved with the event.
     */
    public FactionCreateEvent(Faction faction, FSession fSession)
    {
        super(faction, fSession);
    }

    @Override
    public HandlerList getHandlers()
    {
        return handerList;
    }

    public static HandlerList getHandlerList()
    {
        return handerList;
    }

}
