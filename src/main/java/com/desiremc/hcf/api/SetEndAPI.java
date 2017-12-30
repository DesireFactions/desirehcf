package com.desiremc.hcf.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;

public class SetEndAPI
{

    public static Location spawn;
    public static Location exit;

    public static void setEndSpawn(Player sender)
    {
        exit = sender.getLocation();
        DesireHCF.getLangHandler().sendRenderMessage(sender, "set_end.spawn", true, false);

        DesireHCF.getConfigHandler().setString("endspawn", BukkitUtils.toString(sender.getLocation()));
    }

    public static void setEndExit(Player sender)
    {
        exit = sender.getLocation();
        DesireHCF.getLangHandler().sendRenderMessage(sender, "set_end.exit", true, false);

        DesireHCF.getConfigHandler().setString("endexit", BukkitUtils.toString(sender.getLocation()));
    }

}
