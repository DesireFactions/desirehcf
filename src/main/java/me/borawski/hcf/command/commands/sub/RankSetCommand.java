package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.RankAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerSessionParser;
import me.borawski.hcf.parser.RankParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;

public class RankSetCommand extends ValidCommand {

    public RankSetCommand() {
        super("set", "Sets a user's rank.", Rank.ADMIN, new String[] { "target", "rank" }, "update");
        addParser(new PlayerSessionParser(), "target");
        addParser(new RankParser(), "rank");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        
        Session target = (Session) args[0];
        Rank rank = (Rank) args[1];

        RankAPI.setRank(sender, label, target, rank);
    }
}
