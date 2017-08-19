package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

public class IntegerParser implements ArgumentParser {

    @Override
    public Integer parseArgument(CommandSender sender, String label, String arg) {
        if (!arg.matches("\\d+")) {
            LANG.sendString(sender, "arg_not_number");
            return null;
        }

        return Integer.parseInt(arg);
    }

}
