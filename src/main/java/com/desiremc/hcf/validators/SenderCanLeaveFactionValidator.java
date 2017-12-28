package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;
import com.desiremc.hcf.session.faction.FactionType;

public class SenderCanLeaveFactionValidator extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.getFactionRank().equals(FactionRank.LEADER) && sender.getFaction().getType() == FactionType.PLAYER)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.leave.cant", true, false);
            return false;
        }
        return true;
    }
}
