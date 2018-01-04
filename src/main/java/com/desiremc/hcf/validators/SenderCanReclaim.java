package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

public class SenderCanReclaim implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (sender.getRank().isDonor() || sender.getRank() == Rank.PARTNER)
        {
            return true;
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.invalid", true, false);
        return false;
    }

}
