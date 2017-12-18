package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import org.bukkit.entity.Player;

public class PlayerHasSafeTimeLeft implements SenderValidator
{

    @Override
    public final boolean validate(Session sender)
    {
        Player p = sender.getPlayer();
        FSession session = FSessionHandler.getFSession(p.getUniqueId());

        if (session.getSafeTimeLeft() <= 0)
        {
            DesireHCF.getLangHandler().sendString(p, "pvp.already-disabled");
            return false;
        }

        return true;
    }

}