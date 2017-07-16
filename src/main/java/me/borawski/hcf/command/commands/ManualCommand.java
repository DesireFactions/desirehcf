package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.ManualUtil;

/**
 * Created by Ethan on 5/17/2017.
 */
public class ManualCommand extends CustomCommand {
    //TODO make this have sub commands such as yt
    
    public ManualCommand() {
        super("Manual", "Opens a manual", Rank.YOUTUBER);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length != 1) {
            LANG.sendUsageMessage(sender, label, "manual");
        }

        if (args[0].equalsIgnoreCase("yt")) {
            System.out.println("Attempting to open manual");
            ManualUtil.openManual(Rank.YOUTUBER, (Player) sender);
        }
    }
}
