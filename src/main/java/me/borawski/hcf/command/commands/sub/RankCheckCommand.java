package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.RankAPI;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;

public class RankCheckCommand extends CustomCommand {

    public RankCheckCommand() {
        super("check", "Check your rank.", Rank.GUEST);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        RankAPI.checkRank(sender, label);
    }

}
