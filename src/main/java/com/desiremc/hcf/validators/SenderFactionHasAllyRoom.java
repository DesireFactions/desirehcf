package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class SenderFactionHasAllyRoom extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        Faction faction = sender.getFaction();

        if (faction.getAllies().size() != 0)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.ally.no_space", true, false);
            return false;
        }
        return true;
    }
}
