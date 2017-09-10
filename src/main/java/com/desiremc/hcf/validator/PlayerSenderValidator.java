package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerSenderValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        if (!(sender instanceof Player)) {
            LANG.sendString(sender, "only_players");
            return false;
        }

        return true;
    }

}
