package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionHandler;

public class ServerHasFactionsValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (FactionHandler.getFactions().size() <= 1)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.list.none", true, false);
            return false;
        }
        return true;
    }
}
