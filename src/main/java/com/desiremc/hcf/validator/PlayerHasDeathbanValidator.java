package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.HCFSession;
import com.desiremc.hcf.DesireHCF;

public class PlayerHasDeathbanValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        HCFSession session = (HCFSession) arg;
        if (!session.hasDeathBan(DesireCore.getCurrentServer()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "no_deathban");
            return false;
        }

        return true;
    }

}
