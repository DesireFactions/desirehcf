package com.desiremc.hcf.validator;

import com.desiremc.hcf.HCFCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.util.FactionsUtils;

public class PlayerHasFactionValidator extends PlayerValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        boolean first = super.validateArgument(sender, label, arg);
        if (!first) {
            return false;
        }
        
        if (FactionsUtils.getFaction((Player) sender) == null) {
            HCFCore.getLangHandler().sendString(sender, "no_faction");
            return false;
        }

        return true;
    }

}
