package me.borawski.hcf.command.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.LivesAPI;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.handler.DeathBanHandler;
import me.borawski.hcf.session.Rank;

public class LivesAddCommand extends CustomCommand {

    public LivesAddCommand() {
        super("add", "add lives", Rank.MODERATOR, "give");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!LivesAPI.validateArguments(sender, label, args)) {
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        Integer amount = Integer.parseInt(args[1]);

        DeathBanHandler.addLives(target, amount);

        LANG.sendRenderMessage(sender, "lives.add",
                "{amount}", Integer.toString(amount),
                "{player}", target.getDisplayName());
        
        LANG.sendRenderMessage(sender, "lives.recieved",
                "{amount}", Integer.toString(amount),
                "{player}", ((Player) sender).getDisplayName());
    }

}