package com.desiremc.hcf.commands.lives;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;

public class LivesAddCommand extends ValidCommand
{

    public LivesAddCommand()
    {
        super("add", "add lives", Rank.MODERATOR, new String[] { "target", "amount" }, "give");
        addParser(new PlayerParser(), "target");
        addParser(new IntegerParser(), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player target = (Player) args[0];
        int amount = (Integer) args[1];

        HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());
        session.addLives(amount);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.add", "{amount}", String.valueOf(amount), "{player}", target.getName());
        DesireHCF.getLangHandler().sendRenderMessage(target, "lives.recieved", "{amount}", String.valueOf(amount), "{sender}", sender.getName());
    }

}