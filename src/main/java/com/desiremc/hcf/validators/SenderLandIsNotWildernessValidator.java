package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.util.FactionsUtils;

public class SenderLandIsNotWildernessValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (FactionsUtils.getFaction(sender.getLocation()).isWilderness())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.wilderness", true, false);
            return false;
        }
        return true;
    }

}
