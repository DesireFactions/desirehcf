package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.HCFSession;

public class PlayerHasDeathbanValidator extends FactionValidator<HCFSession>
{

    @Override
    public boolean factionsValidateArgument(HCFSession sender, String[] label, HCFSession arg)
    {
        if (!sender.hasDeathBan())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getPlayer(), "no_deathban");
            return false;
        }

        return true;
    }
}
