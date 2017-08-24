package me.borawski.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.util.FactionsUtils;

public class PlayerHasFactionValidator extends PlayerSenderValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        if (FactionsUtils.getFaction((Player) sender).getId().equals("0")) {
            LANG.sendString(sender, "no_faction");
            return false;
        }

        return true;
    }

}
