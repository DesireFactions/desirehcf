package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class SenderFactionOfficerValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.getFactionRank().ordinal() < FactionRank.OFFICER.ordinal())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.not_officer");
            return false;
        }

        return true;
    }

}
