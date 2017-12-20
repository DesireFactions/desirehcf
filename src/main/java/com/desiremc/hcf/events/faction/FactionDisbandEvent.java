package com.desiremc.hcf.events.faction;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.desiremc.hcf.session.faction.Faction;

public class FactionDisbandEvent extends FactionEvent implements Cancellable
{

    private boolean cancelled;
    
    public FactionDisbandEvent(Faction faction)
    {
        super(faction);
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

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

}
