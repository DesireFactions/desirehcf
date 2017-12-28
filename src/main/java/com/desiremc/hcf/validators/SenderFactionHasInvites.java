package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class SenderFactionHasInvites extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        Faction faction = sender.getFaction();

        if (faction.getInvites().size() == 0)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.invites.no_invites", true, false);
            return false;
        }
        return true;
    }
}
