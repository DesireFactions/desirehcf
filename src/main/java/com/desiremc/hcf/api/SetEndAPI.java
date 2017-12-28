package com.desiremc.hcf.api;

import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEndAPI
{

    private static void handle(CommandSender sender, String configName, String messageName)
    {
        Player player = (Player) sender;
        DesireHCF.getConfigHandler().setString(configName, BukkitUtils.toString(player.getLocation()));
        DesireHCF.getLangHandler().sendRenderMessage(sender, messageName, true, false);
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
