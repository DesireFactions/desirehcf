package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.api.LangHandler;

public interface ArgumentParser {
    
    public static final LangHandler LANG = DesireCore.getLangHandler();
    
    public Object parseArgument(CommandSender sender, String label, String arg);
    
}
