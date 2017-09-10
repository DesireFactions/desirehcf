package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.api.LangHandler;

public interface ArgumentParser {
    
    public static final LangHandler LANG = Core.getLangHandler();
    
    public Object parseArgument(CommandSender sender, String label, String arg);
    
}
