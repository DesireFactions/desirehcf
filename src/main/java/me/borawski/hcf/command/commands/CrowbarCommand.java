package me.borawski.hcf.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.util.CrowbarUtils;

public class CrowbarCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] array) {
        if (command.getName().equalsIgnoreCase("crowbar")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.hasPermission("hcf.crowbar")) {
                    player.getInventory().addItem(CrowbarUtils.getNewCrowbar());
                } else {
                    player.sendMessage("No permission!");
                }
            } else {
                commandSender.sendMessage("No permission!");
            }
        }
        return true;
    }
}