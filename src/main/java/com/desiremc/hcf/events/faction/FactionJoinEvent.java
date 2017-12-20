package com.desiremc.hcf.events.faction;

import org.bukkit.event.HandlerList;

import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class FactionJoinEvent extends FactionPlayerEvent
{

    public FactionJoinEvent(Faction faction, FSession hcfSession)
    {
        super(faction, hcfSession);
    }

    private static HandlerList handlerList = new HandlerList();

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
