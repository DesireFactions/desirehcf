package me.borawski.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.FriendsAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;
import me.borawski.hcf.validator.SenderIncommingFriendRequestsValidator;

public class FriendListIncomingCommand extends ValidCommand {

    public FriendListIncomingCommand() {
        super("incoming", "Lists incoming friend requests.", Rank.GUEST, new String[] {});
        addValidator(new PlayerSenderValidator());
        addValidator(new SenderIncommingFriendRequestsValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        FriendsAPI.listIncomming((Player) sender);
    }

}
