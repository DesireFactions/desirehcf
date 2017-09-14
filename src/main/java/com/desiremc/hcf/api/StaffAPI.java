package com.desiremc.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.StaffHandler;

public class StaffAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();
    private static final StaffHandler STAFF = StaffHandler.getInstance();

    public static void freeze(CommandSender sender, Player player)
    {
        if (STAFF.toggleFreeze(player))
        {
            LANG.sendRenderMessage(sender, "staff.froze_target", "{target}", player.getDisplayName());
        }
    }

    public static void toggleStaffMode(Player sender)
    {
        if (STAFF.toggleStaffMode(sender))
        {
            LANG.sendString(sender, "staff.staff_mode_on");
            return;
        }

        LANG.sendString(sender, "staff.staff_mode_off");
    }

    public static void clicksPerSecondTest(CommandSender sender, Player player)
    {
        if (STAFF.CPSTest(player))
        {
            LANG.sendRenderMessage(sender, "staff.cps_test_already_running", "{target}", player.getDisplayName());
            return;
        }

        STAFF.startCPSTestForPlayer(player);
        LANG.sendRenderMessage(sender, "staff.cps_test", "{taget}", player.getDisplayName());
    }

    public static void toggleInvisibility(Player player)
    {
        STAFF.toggleInvisibility(player);
    }

    public static void mount(Player passenger, Player target)
    {
        STAFF.mount(passenger, target);
    }

}
