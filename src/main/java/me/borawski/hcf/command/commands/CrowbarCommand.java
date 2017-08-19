package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.CrowbarUtils;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class CrowbarCommand extends ValidCommand {

    public CrowbarCommand() {
        super("crowbar", "Spawn in a new crowbar.", Rank.MODERATOR, new String[] {});
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        ((Player) sender).getInventory().addItem(CrowbarUtils.getNewCrowbar());
        LANG.sendString(sender, "crowbar");
    }
}