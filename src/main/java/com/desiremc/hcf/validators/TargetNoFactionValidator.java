package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class TargetNoFactionValidator extends FactionValidator<FSession>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (!arg.getFaction().isWilderness())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.invite.has_faction", "{player}", arg.getName());
            return false;
        }

        return true;
    }
}
