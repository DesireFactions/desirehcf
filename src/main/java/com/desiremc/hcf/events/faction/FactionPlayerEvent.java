package com.desiremc.hcf.events.faction;

import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public abstract class FactionPlayerEvent extends FactionEvent
{

    /**
     * The {@link FSession} involved with the event.
     */
    protected FSession fSession;

    /**
     * @param faction the {@link Faction} involved with the event.
     * @param fSession the {@link FSession} involved with the event.
     */
    public FactionPlayerEvent(Faction faction, FSession fSession)
    {
        super(faction);
        this.fSession = fSession;
    }

    /**
     * @return the {@link FSession} involved with the event.
     */
    public FSession getFSession()
    {
        return fSession;
    }

}
