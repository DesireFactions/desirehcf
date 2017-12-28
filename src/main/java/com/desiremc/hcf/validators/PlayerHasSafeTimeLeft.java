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
        FSession session = FSessionHandler.getGeneralFSession(p.getUniqueId());

        if (session.getSafeTimeLeft() <= 0)
        {
            DesireHCF.getLangHandler().sendRenderMessage(p, "pvp.already-disabled", true, false);
            return false;
        }

        return true;
    }

}