package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionState;

public class FactionNameNotTaken extends FactionValidator<String>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, String arg)
    {
        boolean allowed = true;

        for (Faction faction : FactionHandler.getFactions())
        {
            if (faction.getName().equalsIgnoreCase(arg) && faction.getState() == FactionState.ACTIVE)
            {
                allowed = false;
            }
        }

        if (!allowed)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.name.name_taken", true, false, "{name}", arg);
            return false;
        }
        return true;
    }
}
