package me.borawski.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.FriendsAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;
import me.borawski.hcf.validator.SenderOutgoingFriendRequestsValidator;

public class FriendListOutgoingCommand extends ValidCommand {

    public FriendListOutgoingCommand() {
        super("outgoing", "Lists incoming friend requests.", Rank.GUEST, new String[] {});
        addValidator(new PlayerSenderValidator());
        addValidator(new SenderOutgoingFriendRequestsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        FriendsAPI.listOutgoing((Player) sender);
    }

}