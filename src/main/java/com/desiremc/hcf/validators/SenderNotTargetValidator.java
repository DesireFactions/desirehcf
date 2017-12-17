package com.desiremc.hcf.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.HCFSession;

public class SenderNotTargetValidator extends FactionValidator<HCFSession>
{

    @Override
    public boolean factionsValidateArgument(HCFSession sender, String[] label, HCFSession arg)
    {
        if (sender == arg)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender.getSession(), "cant_to_self");
            return false;
        }
        return true;
    }

}
