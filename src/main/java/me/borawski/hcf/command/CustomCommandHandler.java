package me.borawski.hcf.command;

import java.util.LinkedList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

/**
 * @author Michael Ziluck
 *
 */
public class CustomCommandHandler implements CommandExecutor {

    private LinkedList<ValidCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ValidCommand command = getCustomCommand(label);
        if (command != null) {
            Session s = sender instanceof Player ? SessionHandler.getSession(sender) : null;
            if (s == null || s.getRank().getId() >= command.getRequiredRank().getId()) {
                command.run(sender, label, args);
            } else {
                Core.getLangHandler().sendString(sender, "no-permissions");
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * @param command
     */
    public void registerCommand(ValidCommand command) {
        if (commands == null) {
            commands = new LinkedList<>();
        }
        Core.getInstance().getCommand(command.name).setExecutor(this);
        commands.add(command);
    }

    private ValidCommand getCustomCommand(String cmd) {
        for (ValidCommand command : commands) {
            if (command.matches(cmd)) {
                return command;
            }
        }
        return null;
    }

}