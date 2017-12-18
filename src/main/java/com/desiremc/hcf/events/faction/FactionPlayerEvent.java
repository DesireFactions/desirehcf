package com.desiremc.hcf.events.faction;

import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public abstract class FactionPlayerEvent extends FactionEvent
{

    /**
     * The {@link FSession} involved with the event.
     */
    protected FSession hcfSession;

    /**
     * @param faction the {@link Faction} involved with the event.
     * @param hcfSession the {@link FSession} involved with the event.
     */
    public FactionPlayerEvent(Faction faction, FSession hcfSession)
    {
        super(faction);
        this.hcfSession = hcfSession;
    }

    /**
     * @return the {@link FSession} involved with the event.
     */
    public FSession getHCFSession()
    {
        return hcfSession;
    }

}
