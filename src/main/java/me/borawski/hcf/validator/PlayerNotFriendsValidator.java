package me.borawski.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Session;
import me.borawski.hcf.util.FriendUtils;

public class PlayerNotFriendsValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        return !FriendUtils.isFriends((Session) arg, ((Player) sender).getUniqueId());
    }

}
