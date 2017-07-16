package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.Utils;

public class SetEndExitCommand extends CustomCommand {

    public SetEndExitCommand() {
        super("exit", "set end exit", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            LANG.sendString(sender, "only-players");
            return;
        }

        Player player = (Player) sender;
        Core.getInstance().getConfig().set("endexit", Utils.toString(player.getLocation()));
        Core.getInstance().saveConfig();
        LANG.sendString(sender, "set_end.exit");
    }
}
