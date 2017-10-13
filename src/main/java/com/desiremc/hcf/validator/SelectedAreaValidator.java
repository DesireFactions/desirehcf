package com.desiremc.hcf.validator;

import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.HCFCore;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectedAreaValidator extends PlayerValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        boolean first = super.validateArgument(sender, label, arg);
        if (!first) {
            return false;
        }
        Player p = (Player) sender;
        Selection s = HCFCore.getWorldEdit().getSelection(p);
        if (s != null && s.getArea() >= 1) {
            return true;
        }
        HCFCore.getLangHandler().sendString(sender, "need_selection");
        return false;
    }

}
