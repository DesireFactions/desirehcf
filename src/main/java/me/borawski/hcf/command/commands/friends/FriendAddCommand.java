package me.borawski.hcf.command.commands.friends;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.FriendsAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerSessionParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.validator.PlayerSenderValidator;
import me.borawski.hcf.validator.SenderNotFriendsValidator;

public class FriendAddCommand extends ValidCommand {

    public FriendAddCommand() {
        super("add", "Add a friend.", Rank.GUEST, new String[] { "target" }, "invite", "befriend");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerSenderValidator());
        addValidator(new SenderNotFriendsValidator(), "target");
    }

    public void validRun(CommandSender sender, String label, Object... args) {
        Session target = (Session) args[0];

        FriendsAPI.addFriend((Player) sender, target);
    }

}
