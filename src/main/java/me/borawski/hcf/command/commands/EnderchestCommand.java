package me.borawski.hcf.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.Utils;

//TODO make this extend CustomCommand
public class EnderchestCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] array) {
        if (command.getName().equalsIgnoreCase("enderchest")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                Session s = SessionHandler.getSession(player);
                if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
                    if (Utils.enderchestDisabled == true) {
                        Utils.enderchestDisabled = false;
                        Core.getInstance().getConfig().set("enderchest-disabled", false);
                        Core.getInstance().saveConfig();
                        player.sendMessage(Core.getLangHandler().getString("enderchest_enabled"));
                    } else {
                        player.sendMessage(Core.getLangHandler().getString("enderchest_disabled"));
                        Core.getInstance().getConfig().set("enderchest-disabled", true);
                        Core.getInstance().saveConfig();
                        Utils.enderchestDisabled = true;
                    }
                } else {
                    player.sendMessage(Utils.noPerms());
                }
            } else {
                commandSender.sendMessage(Utils.noPerms());
            }
        }
        return true;
    }
}