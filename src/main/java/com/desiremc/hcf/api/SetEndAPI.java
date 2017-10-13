package com.desiremc.hcf.api;

import com.desiremc.core.utils.Utils;
import com.desiremc.hcf.HCFCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEndAPI
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

    private static void handle(CommandSender sender, String configName, String messageName)
    {
        Player player = (Player) sender;
        HCFCore.getConfigHandler().setString(configName, Utils.toString(player.getLocation()));
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
