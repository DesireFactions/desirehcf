package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class SelectedAreaValidator extends PlayerValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        boolean first = super.validateArgument(sender, label, arg);
        if (!first) {
            return false;
        }
        Player p = (Player) sender;
        Selection s = DesireCore.getWorldEdit().getSelection(p);
        if (s != null && s.getArea() >= 1) {
            return true;
        }
        LANG.sendString(sender, "need_selection");
        return false;
    }

}