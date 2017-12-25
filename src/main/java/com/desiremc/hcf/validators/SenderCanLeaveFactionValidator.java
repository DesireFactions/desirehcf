package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class SenderCanLeaveFactionValidator extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.getFactionRank().equals(FactionRank.LEADER))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.leave.cant", true, false);
            return false;
        }
        return true;
    }
}
