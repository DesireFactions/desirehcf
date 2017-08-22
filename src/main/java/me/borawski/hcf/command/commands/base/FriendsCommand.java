package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.command.commands.sub.FriendAcceptCommand;
import me.borawski.hcf.command.commands.sub.FriendAddCommand;
import me.borawski.hcf.command.commands.sub.FriendDeclineCommand;
import me.borawski.hcf.command.commands.sub.FriendListCommand;
import me.borawski.hcf.command.commands.sub.FriendListIncomingCommand;
import me.borawski.hcf.command.commands.sub.FriendListOutgoingCommand;
import me.borawski.hcf.command.commands.sub.FriendRemoveCommand;
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
