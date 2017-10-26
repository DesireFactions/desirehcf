package com.desiremc.hcf.validator;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerHasSafeTimeLeft extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Player p = (Player) sender;
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if(session.getSafeTimeLeft() <= 0)
        {
            DesireHCF.getLangHandler().sendString(sender, "pvp.already-disabled");
            return false;
        }

        return true;
    }

}