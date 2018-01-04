package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.util.FactionsUtils;

public class SenderLandIsOwnValidator extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (!FactionsUtils.getFaction(sender.getLocation()).equals(sender.getFaction()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.not_own", true, false);
            return false;
        }
        return true;
    }
}
