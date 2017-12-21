package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class SenderFactionCanKickValidator extends FactionValidator<FSession>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (sender.getFactionRank().ordinal() <= arg.getFactionRank().ordinal())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.kick.invalid");
            return false;
        }
        return true;
    }
}
