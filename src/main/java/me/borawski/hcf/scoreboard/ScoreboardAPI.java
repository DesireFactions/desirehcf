package me.borawski.hcf.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardAPI extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("EasyScoreboards have been loaded correctly.");
    }
}
