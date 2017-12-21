package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class PlayerHasFactionInviteValidator extends FactionValidator<FSession>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (!sender.getFaction().getInvites().contains(arg))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.invite.none_target", "{player}", arg.getName());
            return false;
        }

        return true;
    }
}
