package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;

/**
 * This validator assumes the player already has an active ClaimSession. If they don't, it will fail gracefully but will
 * not send the player an error message.
 * 
 * @author Michael Ziluck
 */
public class SenderClaimHasPointOneValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (!sender.hasClaimSession())
        {
            return false;
        }
        else if (!sender.getClaimSession().hasPointOne())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.claims.need_point_one", true, false);
            return false;
        }
        return true;
    }

}
