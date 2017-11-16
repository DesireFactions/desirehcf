package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;

public class PlayerHasDeathbanValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        HCFSession session = (HCFSession) arg;
        if (!session.hasDeathBan())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "no_deathban");
            return false;
        }

        return true;
    }

}
