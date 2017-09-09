package com.desiremc.hcf.parser;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerParser implements ArgumentParser {

    @SuppressWarnings("deprecation")
    @Override
    public Player parseArgument(CommandSender sender, String label, String arg) {
        Player player = Bukkit.getPlayer(arg);
        
        if (player == null) {
            LANG.sendString(sender, "player_not_found");
        }
        
        return player;
    }

}
