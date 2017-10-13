package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.hcf.session.FactionSessionHandler;

public class FactionSessionParser implements ArgumentParser {

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg) {
        return FactionSessionHandler.getFactionSession(arg);
    }

}
