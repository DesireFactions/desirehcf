package me.borawski.hcf.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

public class FactionsUtils {

    public static Faction getFaction(String name) {
        return Factions.getInstance().getByTag(name);
    }

    public static Faction getFaction(Player p) {
        FPlayer fp = FPlayers.getInstance().getByPlayer(p);

        return fp != null ? fp.getFaction() : null;
    }

    public static Faction getFaction(Location loc) {
        FLocation fLoc = new FLocation(loc);
        Faction f = Board.getInstance().getFactionAt(fLoc);
        return f;
    }

}
