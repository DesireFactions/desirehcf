package me.borawski.hcf.command.commands.friends;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class FriendsCommand extends ValidBaseCommand {

    public FriendsCommand() {
        super("friends", "control friends list", Rank.GUEST, "friend");
        addSubCommand(new FriendAcceptCommand());
        addSubCommand(new FriendDeclineCommand());
        addSubCommand(new FriendAddCommand());
        addSubCommand(new FriendRemoveCommand());
        addSubCommand(new FriendListCommand());
        addSubCommand(new FriendListIncomingCommand());
        addSubCommand(new FriendListOutgoingCommand());
    }

}
