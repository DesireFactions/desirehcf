package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;
import me.borawski.hcf.util.FriendUtils;
import me.borawski.hcf.util.PlayerUtils;

public class FriendAcceptCommand extends CustomCommand {

    public FriendAcceptCommand() {
        super("accept", "Accept a friend request.", Rank.GUEST, "confirm");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            LANG.sendString(sender, "only-players");
            return;
        }
        if (args.length == 0) {
            if (!PlayerUtils.hasPlayed(args[0])) {
                LANG.sendString(sender, "player_not_found");
                return;
            }
            Session target = SessionHandler.getSession(PlayerUtils.getUUIDFromName(args[0]));
            String targetName = ChatUtils.getNameWithRankColor(target.getUniqueId(), false);
            Session session = SessionHandler.getSession((Player) sender);

            if (FriendUtils.isFriends(session, target.getUniqueId())) {
                LANG.sendRenderMessage(sender, "friend.already_friends", "{player}", targetName);
                return;
            }

            if (!FriendUtils.hasRequest(session, target.getUniqueId())) {
                // Sender has not already sent a friend request to this
                // player. //
                LANG.sendRenderMessage(sender, "friend.no_friend_request", "{player}", targetName);
                return;
            }

            LANG.sendRenderMessage(sender, "friend.no_friend_request", "{player}", targetName);
            LANG.sendRenderMessage(sender, "friend.are_now_friends", "{player}", targetName);

            FriendUtils.acceptFriendRequest(session, target.getUniqueId(), true);
            FriendUtils.acceptFriendRequest(target, session.getUniqueId(), false);
        } else {
            LANG.sendUsageMessage(sender, label, "player");
        }
    }

}
