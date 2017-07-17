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

public class FriendDeclineCommand extends CustomCommand {

    public FriendDeclineCommand() {
        super("decline", "Decline a friend request.", Rank.GUEST, "deny");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            LANG.sendString(sender, "only-player");
            return;
        }
        if (args.length == 0) {
            if (!PlayerUtils.hasPlayed(args[0])) {
                LANG.sendString(sender, "player_not_found");
                return;
            }
            Session session = SessionHandler.getSession((Player) sender);
            Session target = SessionHandler.getSession(PlayerUtils.getUUIDFromName(args[0]));
            String targetName = ChatUtils.getNameWithRankColor(target.getUniqueId(), false);

            if (FriendUtils.isFriends(session, target.getUniqueId())) {
                LANG.sendRenderMessage(sender, "friend.already_friends", "{player}", targetName);
                return;
            }

            if (!FriendUtils.hasRequest(session, target.getUniqueId())) {
                LANG.sendRenderMessage(sender, "friend.no_friend_request", "{player}", targetName);
                return;
            }

            FriendUtils.denyFriendRequest(session, target.getUniqueId(), true);
            FriendUtils.denyFriendRequest(target, session.getUniqueId(), false);
            LANG.sendRenderMessage(sender, "friend.denied_friend_request", "{player}", targetName);
        }
    }

}
