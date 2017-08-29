package me.borawski.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.StaffHandler;

public class StaffAPI {

    private static final LangHandler LANG = Core.getLangHandler();
    private static final StaffHandler STAFF = Core.getStaffHandler();

    public static void freeze(CommandSender sender, Player player) {
        if (STAFF.toggleFreeze(player)) {
            LANG.sendRenderMessage(sender, "staff.froze_target", "{target}", player.getDisplayName());
        }
    }

    public static void toggleStaffMode(Player sender) {
        if (STAFF.toggleStaffMode(sender)) {
            LANG.sendString(sender, "staff.staff_mode_on");
            return;
        }

        LANG.sendString(sender, "staff.staff_mode_off");
    }

    public static void clicksPerSecondTest(CommandSender sender, Player player) {
        if (STAFF.CPSTest(player)) {
            LANG.sendRenderMessage(sender, "staff.cps_test_already_running", "{target}", player.getDisplayName());
            return;
        }

        LANG.sendRenderMessage(sender, "staff.cps_test", "{taget}", player.getDisplayName());
    }

}
