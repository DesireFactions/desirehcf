package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;

/**
 * This validator assumes that the sender has a faction. If they do not, it will fail gracefully but not send an error
 * message.
 * 
 * @author Michael Ziluck
 */
public class SenderHasFactionHomeValidator extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (!sender.hasFaction())
        {
            sender.sendMessage("the developers forgot to check if the player has a faction first");
            return false;
        }
        if (sender.getFaction().getHomeLocation() == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.not_set");
            return false;
        }
        return true;
    }

}
