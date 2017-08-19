package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.RankAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;

/**
 * @author Ryan Radomski
 * 
 */
public class RankListCommand extends ValidCommand {

    public RankListCommand() {
        super("list", "list all the ranks", Rank.GUEST, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        RankAPI.listRanks(sender);
    }

}
