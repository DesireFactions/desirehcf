package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.Faction;

/**
 * This validator assumes that the sender definitely has a faction. It will fail gracefully if it does not, but no error
 * message will be displayed to the sender.
 * 
 * @author Michael Ziluck
 */
public class SenderFactionSizeValidator extends FactionSenderValidator
{

    private int size;

    /**
     * @param size the required size to check for.
     */
    public SenderFactionSizeValidator(int size)
    {
        this.size = size;
    }

    @Override
    public boolean factionsValidate(HCFSession sender)
    {
        if (!sender.hasFaction())
        {
            return false;
        }
        
        Faction faction = sender.getFaction();

        if (faction.getMemberSize() < size)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.size_too_small",
                    "{size}", size - 1,
                    "{s}", size - 1 != 1 ? "s" : "");
            return false;
        }

        return true;
    }

}
