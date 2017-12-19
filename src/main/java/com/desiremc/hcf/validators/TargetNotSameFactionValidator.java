package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class TargetNotSameFactionValidator extends FactionValidator<FSession>
{

    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (sender.getFaction() == arg.getFaction())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.member");
            return false;
        }

        return true;
    }

}
