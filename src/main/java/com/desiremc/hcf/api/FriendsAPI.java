package com.desiremc.hcf.api;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.FriendUtils;
import com.desiremc.hcf.util.PlayerUtils;

public class FriendsAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public static void acceptRequest(Player sender, Session target)
    {
        FriendUtils.acceptFriendRequest(target, sender.getUniqueId(), true);

        LANG.sendRenderMessage(sender, "accepted_friend_request", "{player}", target.getName());
    }

    public static void removeFriend(Player sender, Session target)
    {
        FriendUtils.removeFriend(target, sender.getUniqueId());

        LANG.sendRenderMessage(sender, "friend.no_longer_friend", "{player}", sender.getDisplayName());
        LANG.sendRenderMessage(sender, "friend.no_longer_friend", "{player}", target.getName());
    }

    public static void addFriend(Player sender, Session target)
    {
        FriendUtils.addFriend(target, sender.getUniqueId());

        LANG.sendRenderMessage(sender, "friend.are_now_friend", "{player}", sender.getDisplayName());
        LANG.sendRenderMessage(sender, "friend.are_now_friend", "{player}", target.getName());
    }

    public static void denyFriend(Player sender, Session target)
    {
        FriendUtils.denyFriendRequest(target, sender.getUniqueId(), true);

        LANG.sendRenderMessage(sender, "friend.denied_friend_request", "{player}", sender.getDisplayName());
    }

    public static void list(Player sender, Session target)
    {
        listPlayers(sender, target.getFriends());
    }

    public static void listIncomming(Player sender)
    {
        listPlayers(sender, SessionHandler.getSession(sender).getIncomingFriendRequests());
    }

    public static void listOutgoing(Player sender)
    {
        listPlayers(sender, SessionHandler.getSession(sender).getIncomingFriendRequests());
    }

    private static void listPlayers(CommandSender sender, List<UUID> players)
    {
        LANG.sendString(sender, "list-header");

        for (UUID x : SessionHandler.getSession(sender).getIncomingFriendRequests())
        {
            LANG.sendRenderMessage(sender, "player", "{player}", PlayerUtils.getName(x));
        }
    }

}
