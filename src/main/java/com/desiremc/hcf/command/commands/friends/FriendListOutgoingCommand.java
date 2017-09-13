package com.desiremc.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.api.FriendsAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;
import com.desiremc.hcf.validator.SenderOutgoingFriendRequestsValidator;

public class FriendListOutgoingCommand extends ValidCommand {

    public FriendListOutgoingCommand() {
        super("outgoing", "Lists incoming friend requests.", Rank.GUEST, new String[] {});
        addValidator(new PlayerValidator());
        addValidator(new SenderOutgoingFriendRequestsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        FriendsAPI.listOutgoing((Player) sender);
    }

}