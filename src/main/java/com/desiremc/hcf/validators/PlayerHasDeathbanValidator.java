package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class PlayerHasDeathbanValidator extends FactionValidator<FSession>
{

    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (!arg.hasDeathBan())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getPlayer(), "no_deathban");
            return false;
        }

        return true;
    }
}
