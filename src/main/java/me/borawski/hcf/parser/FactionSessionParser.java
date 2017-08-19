package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.session.FactionSessionHandler;

public class FactionSessionParser implements ArgumentParser {

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg) {
        return FactionSessionHandler.getFactionSession(arg);
    }

}
