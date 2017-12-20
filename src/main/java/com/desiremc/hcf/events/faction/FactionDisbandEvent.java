package com.desiremc.hcf.events.faction;

import org.bukkit.event.HandlerList;

import com.desiremc.hcf.session.faction.Faction;

public class FactionDisbandEvent extends FactionEvent
{

    private static HandlerList handlerList = new HandlerList();

    public FactionDisbandEvent(Faction faction)
    {
        super(faction);
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
