package com.desiremc.hcf.events.faction;

import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.Faction;

public class FactionPlayerEvent extends FactionEvent
{

    /**
     * The {@link HCFSession} involved with the event.
     */
    protected HCFSession hcfSession;

    /**
     * @param faction the {@link Faction} involved with the event.
     * @param hcfSession the {@link HCFSession} involved with the event.
     */
    public FactionPlayerEvent(Faction faction, HCFSession hcfSession)
    {
        super(faction);
        this.hcfSession = hcfSession;
    }

    /**
     * @return the {@link HCFSession} involved with the event.
     */
    public HCFSession getHCFSession()
    {
        return hcfSession;
    }

}
