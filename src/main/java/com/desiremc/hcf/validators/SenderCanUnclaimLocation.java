package com.desiremc.hcf.validators;

import com.desiremc.core.utils.BoundedArea;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionHandler;

public class SenderCanUnclaimLocation extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        BoundedArea claim = FactionHandler.getArea(sender.getLocation());

        for (BoundedArea area : sender.getFaction().getClaims())
        {
            if (area.distance(claim) > 1)
            {
                DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.unclaim.not_touching", true, false);
                return false;
            }
        }

        return true;
    }
}
