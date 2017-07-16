package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.gui.PlayerInfoGUI;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.PlayerUtils;

/**
 * Created by Ethan on 3/8/2017.
 */
public class InfoCommand extends CustomCommand {

    public InfoCommand() {
        super("info", "Get a user's information.", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            LANG.sendString(sender, "only-players");
            return;
        }
        
        if (args.length != 1) {
            LANG.sendUsageMessage(sender, label, "player");
            return;
        }

        Player player = (Player) sender;
        String name = args[0];
        Session s = SessionHandler.getSession(PlayerUtils.getUUIDFromName(name));

        if (s == null) {
            LANG.sendRenderMessage(sender, "could_not_retrieve", "{name}", name);
            return;
        }

        PlayerInfoGUI.crossTarget.put(player.getUniqueId(), s);
        new PlayerInfoGUI(player).show();
    }
}
