package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

public class PlayerHasLivesValidator implements SenderValidator
{

    @Override
    public final boolean validate(Session sender)
    {
        FSession session = FSessionHandler.getGeneralFSession(sender.getUniqueId());
        if (session.getLives() <= 0 && !session.getRank().isManager())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.no_lives", true, false);
            return false;
        }
        return true;
    }

}
