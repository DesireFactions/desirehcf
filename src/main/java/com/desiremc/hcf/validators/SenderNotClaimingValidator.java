package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;

public class SenderNotClaimingValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.hasClaimSession())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.claims.claiming");
            return false;
        }

        return true;
    }

}
