package com.desiremc.hcf.command.commands.lives;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.LivesAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.IntegerParser;
import com.desiremc.hcf.parser.PlayerParser;
import com.desiremc.hcf.session.Rank;

public class LivesAddCommand extends ValidCommand
{

    public LivesAddCommand()
    {
        super("add", "add lives", Rank.MODERATOR, new String[]{"target", "amount"}, "give");
        addParser(new PlayerParser(), "target");
        addParser(new IntegerParser(), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player target = (Player) args[0];
        Integer amount = (Integer) args[1];

        LivesAPI.addLives(sender, target, amount);
    }

}