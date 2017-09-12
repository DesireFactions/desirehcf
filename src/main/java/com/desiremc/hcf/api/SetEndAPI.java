package com.desiremc.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.util.Utils;

public class SetEndAPI {

    private static final LangHandler LANG = Core.getLangHandler();
    
    private static void handle(CommandSender sender, String configName, String messageName) {
        Player player = (Player) sender;
        Core.getConfigHandler().setString(configName, Utils.toString(player.getLocation()));
        Core.getInstance().saveConfig();
        LANG.sendString(sender, messageName);
    }

    public static void setEndSpawn(CommandSender sender, String configName, String messageName) {
        handle(sender, configName, messageName);
    }

    public static void setEndExit(CommandSender sender, String configName, String messageName) {
        handle(sender, configName, messageName);
    }
    
}
