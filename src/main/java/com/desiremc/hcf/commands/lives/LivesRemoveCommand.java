package com.desiremc.hcf.commands.lives;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.api.LivesAPI;

public class LivesRemoveCommand extends ValidCommand
{

    public LivesRemoveCommand()
    {
        super("remove", "remove lives", Rank.MODERATOR, new String[]{"target", "amount"}, "take");
        addParser(new PlayerParser(), "target");
        addParser(new IntegerParser(), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player target = (Player) args[0];
        Integer amount = (Integer) args[1];

        LivesAPI.takeLives(sender, target, amount);
    }

}