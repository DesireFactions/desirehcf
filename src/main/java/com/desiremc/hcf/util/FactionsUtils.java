package com.desiremc.hcf.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.session.HCFSession;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

public class FactionsUtils {

    private static final Faction WILD = Factions.getInstance().getWilderness();

    public static Faction getFaction(String name) {
        Faction f = Factions.getInstance().getByTag(name);
        return f == WILD ? null : f;
    }

    public static Faction getFaction(Player p) {
        FPlayer fp = FPlayers.getInstance().getByPlayer(p);

        return fp != null ? fp.getFaction() == WILD ? null : fp.getFaction() : null;
    }

    public static Faction getFaction(Location loc) {
        FLocation fLoc = new FLocation(loc);
        Faction f = Board.getInstance().getFactionAt(fLoc);
        return f == WILD ? null : f;
    }

    public static Faction getFaction(HCFSession s) {
        return getFaction(s.getPlayer());
    }

}
