package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.FriendsAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerSessionParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.validator.PlayerSenderValidator;
import me.borawski.hcf.validator.SenderFriendRequestValidator;
import me.borawski.hcf.validator.SenderIsFriendsValidtor;

public class FriendDeclineCommand extends ValidCommand {

    public FriendDeclineCommand() {
        super("decline", "Decline a friend request.", Rank.GUEST, new String[] { "target" }, "deny");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerSenderValidator());
        addValidator(new SenderFriendRequestValidator(), "target");
        addValidator(new SenderIsFriendsValidtor(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Session target = (Session) args[0];

        FriendsAPI.denyFriend((Player) sender, target);
    }

}
