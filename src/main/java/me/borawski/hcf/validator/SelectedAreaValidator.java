package me.borawski.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.borawski.hcf.Core;

public class SelectedAreaValidator extends PlayerSenderValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        super.validateArgument(sender, label, arg);
        Player p = (Player) sender;
        Selection s = Core.getWorldEdit().getSelection(p);
        if (s != null && s.getArea() >= 1) {
            return true;
        }
        LANG.sendString(sender, "need_selection");
        return false;
    }

}
