package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class PlayerSessionParser implements ArgumentParser {

    @Override
    public Session parseArgument(CommandSender sender, String label, String arg) {
        Session s = SessionHandler.getSession(arg);

        if (s == null) {
            LANG.sendString(sender, "player-not-found");
            return null;
        }

        return s;
    }

}
