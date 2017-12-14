package com.desiremc.hcf.events.faction;

import org.bukkit.event.Cancellable;

import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.Faction;

public class FactionCreateEvent extends FactionPlayerEvent implements Cancellable
{

    protected boolean cancelled;

    /**
     * @param faction the {@link Faction} involved with the event.
     * @param hcfSession the {@link HCFSession} involved with the event.
     */
    public FactionCreateEvent(Faction faction, HCFSession hcfSession)
    {
        super(faction, hcfSession);
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

}
