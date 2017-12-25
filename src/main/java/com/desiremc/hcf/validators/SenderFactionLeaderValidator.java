package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class SenderFactionLeaderValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.getFactionRank() != FactionRank.LEADER)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.not_leader", true, false);
            return false;
        }

        return true;
    }

}
