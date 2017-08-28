package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class PVPCommand extends ValidCommand {

    public PVPCommand() {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Session s = SessionHandler.getSession(sender);
        
        s.setSafeTimeLeft(0);
        LANG.sendString(sender, "pvp.disabled");
    }

}
