package com.desiremc.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.util.Utils;

public class SetEndAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    private static void handle(CommandSender sender, String configName, String messageName)
    {
        Player player = (Player) sender;
        DesireCore.getConfigHandler().setString(configName, Utils.toString(player.getLocation()));
        LANG.sendString(sender, messageName);
    }

    public static void setEndSpawn(CommandSender sender, String configName, String messageName)
    {
        handle(sender, configName, messageName);
    }

    public static void setEndExit(CommandSender sender, String configName, String messageName)
    {
        handle(sender, configName, messageName);
    }

}
