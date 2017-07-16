package me.borawski.hcf.command.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.LivesAPI;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.handler.DeathBanHandler;
import me.borawski.hcf.session.Rank;

public class LivesRemoveCommand extends CustomCommand {

    public LivesRemoveCommand() {
        super("remove", "remove lives", Rank.MODERATOR, "take");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!LivesAPI.validateArguments(sender, label, args)) {
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        Integer amount = Integer.parseInt(args[1]);

        DeathBanHandler.takeLives(target, amount);

        LANG.sendRenderMessage(sender, "lives.remove",
                "{amount}", Integer.toString(amount),
                "{player}", target.getDisplayName());

        LANG.sendRenderMessage(sender, "lives.lost",
                "{amount}", Integer.toString(amount),
                "{player}", ((Player) sender).getDisplayName());
    }

}