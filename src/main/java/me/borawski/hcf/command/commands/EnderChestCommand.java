package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.Utils;

public class EnderChestCommand extends ValidCommand {

    public EnderChestCommand() {
        super("enderchest", "Toggle the ender chest.", Rank.ADMIN, new String[] {}, "chest", "ender");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        if (Utils.enderchestDisabled == true) {
            Utils.enderchestDisabled = false;
            Core.getInstance().getConfig().set("enderchest-disabled", false);
            Core.getInstance().saveConfig();
            LANG.sendString(sender, "enderchest.enabled");
        } else {
            LANG.sendString(sender, "enderchest.disabled");
            Core.getInstance().getConfig().set("enderchest-disabled", true);
            Core.getInstance().saveConfig();
            Utils.enderchestDisabled = true;
        }
    }
}