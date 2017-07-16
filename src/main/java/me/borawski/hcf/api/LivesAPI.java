package me.borawski.hcf.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;

public class LivesAPI {

    private final static LangHandler LANG = Core.getLangHandler();

    @SuppressWarnings("deprecation")
    public static boolean validateArguments(CommandSender sender, String label, String[] args) {
        if (args.length != 2) {
            LANG.sendUsageMessage(sender, label, "target", "amount");
            return false;
        }

        if (!(sender instanceof Player)) {
            LANG.sendString(sender, "only-players");
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            LANG.sendString(sender, "player_not_found");
            LANG.sendUsageMessage(sender, label, "target", "amount");
            return false;
        }

        if (!args[1].matches("\\d+")) {
            LANG.sendString(sender, "arg_not_number");
            LANG.sendUsageMessage(sender, label, "target", "amount");
            return false;
        }

        return true;
    }
}
