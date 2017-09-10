package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.util.FactionsUtils;

public class PlayerHasFactionValidator extends PlayerSenderValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        boolean first = super.validateArgument(sender, label, arg);
        if (first == false) {
            return false;
        }
        
        if (FactionsUtils.getFaction((Player) sender) == null) {
            LANG.sendString(sender, "no_faction");
            return false;
        }

        return true;
    }

}
