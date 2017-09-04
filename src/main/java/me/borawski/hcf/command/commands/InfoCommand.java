package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.old_gui.PlayerInfoGUI;
import me.borawski.hcf.parser.PlayerSessionParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class InfoCommand extends ValidCommand {

    public InfoCommand() {
        super("info", "Get a user's information.", Rank.ADMIN, new String[] { "target" });
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Player player = (Player) sender;
        Session target = (Session) args[0];

        PlayerInfoGUI.crossTarget.put(player.getUniqueId(), target);
        new PlayerInfoGUI(player).show();
    }
}
