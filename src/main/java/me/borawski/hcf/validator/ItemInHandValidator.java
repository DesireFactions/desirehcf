package me.borawski.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemInHandValidator extends PlayerSenderValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        boolean first = super.validateArgument(sender, label, arg);
        if (!first) {
            return false;
        }
        
        Player p = (Player) sender;
        if (p.getInventory().getItemInMainHand() == null) {
            LANG.sendString(sender, "item_in_hand");
            return false;
        }
        
        return true;
    }
    
}
