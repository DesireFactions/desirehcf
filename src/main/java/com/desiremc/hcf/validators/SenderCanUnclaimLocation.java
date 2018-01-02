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

        for (BoundedArea area1 : sender.getFaction().getClaims())
        {
            if (area1 == claim)
            {
                continue;
            }
            for (BoundedArea area2 : sender.getFaction().getClaims())
            {
                if (area2 == claim)
                {
                    continue;
                }
                if (area2.distance(area1) > 0)
                {
                    DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.unclaim.not_touching", true, false);
                    return false;
                }
            }
        }

        return true;
    }
}
