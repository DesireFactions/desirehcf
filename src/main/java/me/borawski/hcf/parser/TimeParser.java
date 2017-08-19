package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.parser.ArgumentParser;

public class TimeParser implements ArgumentParser {
    
    @Override
    public Object parseArgument(CommandSender sender, String label, String arg) {
        if (arg.matches("\\d+")) {
            return Long.parseLong(arg);
        }

        LANG.sendString(sender, "not_time");
        return null;
    }

}
