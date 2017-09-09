package com.desiremc.hcf.util;

import java.util.UUID;

import com.desiremc.hcf.session.AchievementManager;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;

public class FriendUtils {

    /*
     * Methods
     */

    public static void addFriend(Session player, UUID target) {
        player.getFriends().add(target);
        if (!player.hasAchievement("first_friend")) {
            player.awardAchievement(AchievementManager.FIRST_FRIEND, true);
        }
    }

    public static void removeFriend(Session player, UUID target) {
        player.getFriends().remove(target);
        saveFriends(player);
    }

    public static void addFriendRequest(Session player, UUID target, boolean incoming) {
        if (incoming) {
            player.getIncomingFriendRequests().add(target);
        } else {
            player.getOutgoingFriendRequests().add(target);
        }
        saveFriends(player);
    }

    public static void acceptFriendRequest(Session player, UUID target, boolean incoming) {
        addFriend(player, target);
        denyFriendRequest(player, target, incoming);
        saveFriends(player);
    }

    public static void denyFriendRequest(Session player, UUID target, boolean incoming) {
        if (incoming) {
            player.getIncomingFriendRequests().remove(target);
        } else {
            player.getOutgoingFriendRequests().remove(target);
        }
    }

    public static boolean hasRequest(Session player, UUID target) {
        return (player.getIncomingFriendRequests().contains(target));
    }

    public static boolean isFriends(Session player, UUID target) {
        return (player.getFriends().contains(target));
    }

    private static void saveFriends(Session player) {
        SessionHandler.getInstance().save(player);
    }

}
