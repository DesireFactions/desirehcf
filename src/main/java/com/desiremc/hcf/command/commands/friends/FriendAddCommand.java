package com.desiremc.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.FriendsAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.validator.PlayerValidator;
import com.desiremc.hcf.validator.SenderNotFriendsValidator;

public class FriendAddCommand extends ValidCommand
{

    public FriendAddCommand()
    {
        super("add", "Add a friend.", Rank.GUEST, new String[]{"target"}, "invite", "befriend");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
        addValidator(new SenderNotFriendsValidator(), "target");
    }

    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        FriendsAPI.addFriend((Player) sender, target);
    }

}
