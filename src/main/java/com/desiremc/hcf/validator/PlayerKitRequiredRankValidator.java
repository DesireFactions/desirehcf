package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;

public class PlayerKitRequiredRankValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Session s = SessionHandler.getSession(sender);
        HKit kit = (HKit) arg;
        
        if (s.getRank().getId() < kit.getRequiredRank().getId())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.no_permission",
                    "{rank}", kit.getRequiredRank().getDisplayName());
            return false;
        }
        
        return true;
    }

}
