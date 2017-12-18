package com.desiremc.hcf.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class SenderNotTargetValidator extends FactionValidator<FSession>
{

    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (sender == arg)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender.getSession(), "cant_to_self");
            return false;
        }
        return true;
    }

}
