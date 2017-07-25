package me.borawski.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.handler.DeathBanHandler;

public class LivesAPI {

    private final static LangHandler LANG = Core.getLangHandler();

    public static void takeLives(CommandSender sender, Player target, Integer amount) {
        String strAmount = Integer.toString(amount);
        String targetName = target.getDisplayName();
        String senderName = ((Player) sender).getDisplayName();

        DeathBanHandler.takeLives(target, amount);

        LANG.sendRenderMessage(sender, "lives.remove",
                "{amount}", strAmount,
                "{player}", targetName);

        LANG.sendRenderMessage(sender, "lives.lost",
                "{amount}", Integer.toString(amount),
                "{player}", senderName);
    }

    public static void addLives(CommandSender sender, Player target, Integer amount) {
        String strAmount = Integer.toString(amount);
        String targetName = target.getDisplayName();
        String senderName = ((Player) sender).getDisplayName();

        DeathBanHandler.addLives(target, amount);

        LANG.sendRenderMessage(sender, "lives.add",
                "{amount}", strAmount,
                "{player}", targetName);

        LANG.sendRenderMessage(sender, "lives.recieved",
                "{amount}", strAmount,
                "{player}", senderName);
    }
}
