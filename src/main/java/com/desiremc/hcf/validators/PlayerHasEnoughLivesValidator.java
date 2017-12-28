package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

public class PlayerHasEnoughLivesValidator implements Validator<Integer>
{
    @Override
    public boolean validateArgument(Session sender, String[] label, Integer arg)
    {
        FSession session = FSessionHandler.getGeneralFSession(sender.getUniqueId());
        if (session.getLives() < arg && !session.getRank().isManager())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.not_enough", true, false);
            return false;
        }
        return true;
    }
}
