package com.desiremc.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.FriendsAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.validator.PlayerValidator;
import com.desiremc.hcf.validator.SenderHasFriendsValidator;

public class FriendListCommand extends ValidCommand
{

    public FriendListCommand()
    {
        super("list", "List all of your friends", Rank.GUEST, new String[]{"target"}, "show");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
        addValidator(new SenderHasFriendsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        FriendsAPI.list((Player) sender, target);
    }

}
