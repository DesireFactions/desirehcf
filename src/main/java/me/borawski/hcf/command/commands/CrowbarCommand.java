package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.CrowbarUtils;

public class CrowbarCommand extends CustomCommand {

    public CrowbarCommand() {
        super("crowbar", "Spawn in a new crowbar.", Rank.MODERATOR);
    }

    public void run(CommandSender sender, String label, String[] array) {
        if (sender instanceof Player) {
            ((Player) sender).getInventory().addItem(CrowbarUtils.getNewCrowbar());
            // TODO need confirmation message
        } else {
            sender.sendMessage("No permission!");
        }
    }
}