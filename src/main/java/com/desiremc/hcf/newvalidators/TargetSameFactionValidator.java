package com.desiremc.hcf.newvalidators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.HCFSession;

public class TargetSameFactionValidator extends FactionValidator<HCFSession>
{

    @Override
    public boolean factionsValidateArgument(HCFSession sender, String[] label, HCFSession arg)
    {
        if (!sender.hasFaction() || sender.getFaction() != arg.getFaction())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.not_member");
            return false;
        }

        return true;
    }

}
