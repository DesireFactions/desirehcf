package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class SenderHasFactionInviteValidator extends FactionValidator<Faction>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, Faction arg)
    {
        if (!arg.getInvites().contains(sender) && arg.isNormal() && !sender.getRank().isManager())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.invite.none", true, false, "{faction}", arg.getName());
            return false;
        }
        return true;
    }
}
