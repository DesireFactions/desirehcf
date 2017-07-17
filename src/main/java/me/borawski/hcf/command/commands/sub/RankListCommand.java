package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.RankAPI;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;

/**
 * @author Ryan Radomski
 * 
 */
public class RankListCommand extends CustomCommand {

    public RankListCommand() {
        super("list", "list all the ranks", Rank.GUEST);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        RankAPI.listRanks(sender);
    }

}
