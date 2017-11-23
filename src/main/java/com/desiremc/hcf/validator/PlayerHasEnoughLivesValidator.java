package com.desiremc.hcf.validator;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import org.bukkit.command.CommandSender;

public class PlayerHasEnoughLivesValidator extends CommandValidator
{
    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        int amount = (int) arg;

        HCFSession session = HCFSessionHandler.getHCFSession(sender);
        if (session.getLives() < amount && !session.getRank().isManager())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.not_enough");
            return false;
        }
        return true;
    }
}
