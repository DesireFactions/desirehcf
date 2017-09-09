package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.session.SessionHandler;

public class SenderHasFriendsValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        return SessionHandler.getSession((Player) sender).getFriends().size() != 0;
    }

}
