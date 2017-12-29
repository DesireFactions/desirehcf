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
        if (sender == null)
        {
            DesireHCF.getInstance().getLogger().severe("SenderFactionOfficerValidator had a null sender.");
            return false;
        }
        if (sender.getFactionRank() == null)
        {
            DesireHCF.getInstance().getLogger().severe("SenderFactionOfficerValidator had a null faction rank.");
            return false;
        }
        if (sender.getFactionRank().ordinal() < FactionRank.OFFICER.ordinal())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.not_officer", true, false);
            return false;
        }

        return true;
    }

}
