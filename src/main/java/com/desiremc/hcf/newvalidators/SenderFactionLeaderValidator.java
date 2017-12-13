package com.desiremc.hcf.newvalidators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class SenderFactionLeaderValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(HCFSession sender)
    {
        if (sender.getFactionRank() != FactionRank.LEADER)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.not_leader");
            return false;
        }

        return true;
    }

}
