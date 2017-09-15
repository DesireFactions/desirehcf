package com.desiremc.hcf.command;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.SessionUtils;

public abstract class ValidBaseCommand extends ValidCommand {

    /**
     * @param name
     * @param description
     * @param permission
     * @param aliases
     */
    public ValidBaseCommand(String name, String description, Rank requiredRank, String... aliases) {
        super(name, description, requiredRank, new String[] {}, aliases);
    }

    public void run(CommandSender sender, String label, String[] args) {
        ValidCommand sub;
        if (args.length == 0 || (sub = getSubCommand(args[0])) == null) {
            help(sender, label);
        } else {
            Session s = sender instanceof Player ? SessionHandler.getSession(sender) : null;
            if (s == null || s.getRank().getId() >= requiredRank.getId()) {
                sub.run(sender, label + " " + args[0], Arrays.copyOfRange(args, 1, args.length));
            } else {
                sender.sendMessage(DesireCore.getLangHandler().getString("no-permissions"));
            }
        }
    }

    /**
     * Sends the help content to the player.
     * 
     * @param sender
     * @param label
     */
    public void help(CommandSender sender, String label) {
        LANG.sendString(sender, "list-header");
        Rank rank = SessionUtils.getRank(sender);

        for (ValidCommand command : subCommands) {
            if (command.getRequiredRank().getId() <= rank.getId()) {
                sender.sendMessage(" §b/" + label + " " + command.getName() + ": §7" + command.getDescription());
            }
        }
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
    }

}