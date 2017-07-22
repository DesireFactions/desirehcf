package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.Utils;

public class EnderchestCommand extends CustomCommand {

    public EnderchestCommand() {
        super("enderchest", "Toggle the ender chest.", Rank.ADMIN, "chest", "ender");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length != 0) {
            LANG.sendUsageMessage(sender, label);
            return;
        }
        
        if (Utils.enderchestDisabled == true) {
            Utils.enderchestDisabled = false;
            Core.getInstance().getConfig().set("enderchest-disabled", false);
            Core.getInstance().saveConfig();
            LANG.sendString(sender, "enderchest_enabled");
        } else {
            LANG.sendString(sender, "enderchest_disabled");
            Core.getInstance().getConfig().set("enderchest-disabled", true);
            Core.getInstance().saveConfig();
            Utils.enderchestDisabled = true;
        }
    }
}