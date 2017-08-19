package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.LangHandler;

public interface ArgumentParser {
    
    public static final LangHandler LANG = Core.getLangHandler();
    
    public Object parseArgument(CommandSender sender, String label, String arg);
    
}
