package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import org.bukkit.entity.Player;

public class PlayerHasSafeTimeLeft implements SenderValidator
{

    @Override
    public final boolean validate(Session sender)
    {
        Player p = sender.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (session.getSafeTimeLeft() <= 0)
        {
            DesireHCF.getLangHandler().sendString(p, "pvp.already-disabled");
            return false;
        }

        return true;
    }

}