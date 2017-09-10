package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.util.FriendUtils;

public class SenderNotFriendsValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        if (FriendUtils.isFriends((Session) arg, ((Player) sender).getUniqueId())) {
            LANG.sendRenderMessage(sender, "friend.already_friends",
                    "{player}", ((Session) arg).getName());
            return false;
        }

        return true;
    }

}
