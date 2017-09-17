package com.desiremc.hcf.scoreboard;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.desiremc.hcf.scoreboard.type.Scoreboard;

public class ScoreboardRegistry
{

    private static ScoreboardRegistry instance;

    private HashMap<Player, Scoreboard> scoreboards;

    public ScoreboardRegistry()
    {
        scoreboards = new HashMap<>();
    }

    public void registerScoreboard(Player p, Scoreboard board)
    {
        scoreboards.put(p, board);
    }

    public void clearScoreboard(Player p)
    {
        Scoreboard sb = scoreboards.remove(p);
        if (sb != null)
        {
            sb.deactivate();
        }
    }

    public static void initialize()
    {
        instance = new ScoreboardRegistry();
    }

    public static ScoreboardRegistry getInstance()
    {
        return instance;
    }

}
