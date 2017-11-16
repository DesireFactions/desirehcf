package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;

public class PlayerHasLivesValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(sender);
        if (session.getLives() <= 0 && !session.getRank().isManager())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.no_lives");
            return false;
        }
        return true;
    }

}
