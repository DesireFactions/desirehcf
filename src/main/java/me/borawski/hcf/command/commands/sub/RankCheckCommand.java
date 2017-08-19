package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.RankAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class RankCheckCommand extends ValidCommand {

    public RankCheckCommand() {
        super("check", "Check your rank.", Rank.GUEST, new String[] {}, "show");
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        RankAPI.checkRank(sender, label);
    }

}
