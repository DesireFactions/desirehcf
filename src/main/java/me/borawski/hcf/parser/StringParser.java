package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

public class StringParser implements ArgumentParser {

    @Override
    public String parseArgument(CommandSender sender, String label, String arg) {
        return arg;
    }

}
