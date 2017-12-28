package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class SenderCanPromoteTargetValidator extends FactionValidator<FSession>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        FactionRank next = FactionRank.getNextRank(arg.getFactionRank());

        if (sender.getFactionRank().ordinal() <= next.ordinal())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.promote.invalid", true, false);
            return false;
        }

        return true;
    }
}
