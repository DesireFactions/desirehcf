package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.old_gui.PlayerInfoGUI;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.validator.PlayerSenderValidator;

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
