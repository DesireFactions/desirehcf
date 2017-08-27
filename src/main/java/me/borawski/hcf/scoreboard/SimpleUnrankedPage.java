package me.borawski.hcf.scoreboard;

import org.bukkit.entity.Player;

public class SimpleUnrankedPage implements BoardPage {
    private String[] content;

    public SimpleUnrankedPage(String[] content) {
        this.content = content;
    }

    @Override
    public void update(Player p) {
        ScoreboardUtils.unrankedSidebarDisplay(p, content);
    }
}