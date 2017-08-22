package me.borawski.hcf.manual;

import org.bukkit.ChatColor;

import java.util.List;

public interface ManualButton {

    String getName();

    ChatColor getColor();

    List<String> getCommands();

}
