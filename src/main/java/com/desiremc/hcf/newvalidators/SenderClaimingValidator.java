package com.desiremc.hcf.newvalidators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.HCFSession;

public class SenderClaimingValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(HCFSession sender)
    {
        if (!sender.hasClaimSession())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.claims.not_claiming");
            return false;
        }

        return true;
    }

}
