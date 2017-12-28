package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;

public class PlayerKitRequiredRankValidator implements Validator<HKit>
{

    @Override
    public boolean validateArgument(Session s, String[] label, HKit kit)
    {

        if (s.getRank().getId() < kit.getRequiredRank().getId())
        {
            DesireHCF.getLangHandler().sendRenderMessage(s, "kits.no_permission", true, false,
                    "{rank}", kit.getRequiredRank().getDisplayName());
            return false;
        }

        return true;
    }

}
