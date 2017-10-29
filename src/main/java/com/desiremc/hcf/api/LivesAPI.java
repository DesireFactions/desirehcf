package com.desiremc.hcf.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.hcf.DesireHCF;

public class LivesAPI
{

    public static void takeLives(CommandSender sender, Player target, Integer amount)
    {
        String strAmount = Integer.toString(amount);
        String targetName = target.getDisplayName();
        String senderName = ((Player) sender).getDisplayName();

        HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());
        session.takeLives(amount);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.remove", "{amount}", strAmount, "{player}", targetName);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.lost", "{amount}", Integer.toString(amount), "{sender}", senderName);
    }

    public static void addLives(CommandSender sender, Player target, Integer amount)
    {
        String strAmount = Integer.toString(amount);
        String targetName = target.getDisplayName();
        String senderName = ((Player) sender).getDisplayName();

        HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());
        session.addLives(amount);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.add", "{amount}", strAmount, "{player}", targetName);
        DesireHCF.getLangHandler().sendRenderMessage(target, "lives.recieved", "{amount}", strAmount, "{sender}", senderName);
    }
}
