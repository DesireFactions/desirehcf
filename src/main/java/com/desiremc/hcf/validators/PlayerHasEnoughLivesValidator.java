package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;

public class PlayerHasEnoughLivesValidator implements Validator<Integer>
{
    @Override
    public boolean validateArgument(Session sender, String[] label, Integer arg)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(sender.getUniqueId());
        if (session.getLives() < arg && !session.getRank().isManager())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.not_enough");
            return false;
        }
        return true;
    }
}
