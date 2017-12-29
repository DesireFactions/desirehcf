package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;

public class SenderHasNotReclaimedValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.hasClaimedRank())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.already_claimed", true, false);
            return false;
        }

        return true;
    }

}
