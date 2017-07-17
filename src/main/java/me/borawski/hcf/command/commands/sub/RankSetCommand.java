package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.RankAPI;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;

public class RankSetCommand extends CustomCommand {

    public RankSetCommand() {
        super("set", "Sets a user's rank.", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            String name = args[0];
            String rank = args[1];

            RankAPI.setRank(sender, label, name, rank, true);
        } else {
            LANG.sendUsageMessage(sender, label, "target", "rank");
        }
    }
}
