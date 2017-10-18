package com.desiremc.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.utils.Utils;
import com.desiremc.hcf.DesireHCF;

public class SetEndAPI
{

    private static void handle(CommandSender sender, String configName, String messageName)
    {
        Player player = (Player) sender;
        DesireHCF.getConfigHandler().setString(configName, Utils.toString(player.getLocation()));
        DesireHCF.getLangHandler().sendString(sender, messageName);
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
