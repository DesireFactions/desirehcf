package com.desiremc.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.FriendsAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.validator.PlayerSenderValidator;
import com.desiremc.hcf.validator.SenderFriendRequestValidator;
import com.desiremc.hcf.validator.SenderIsFriendsValidator;

public class FriendDeclineCommand extends ValidCommand {

    public FriendDeclineCommand() {
        super("decline", "Decline a friend request.", Rank.GUEST, new String[] { "target" }, "deny");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerSenderValidator());
        addValidator(new SenderFriendRequestValidator(), "target");
        addValidator(new SenderIsFriendsValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Session target = (Session) args[0];

        FriendsAPI.denyFriend((Player) sender, target);
    }

}
