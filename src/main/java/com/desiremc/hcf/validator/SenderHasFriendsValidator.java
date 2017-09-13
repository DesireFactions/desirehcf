package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.session.SessionHandler;

public class SenderHasFriendsValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        return SessionHandler.getSession(sender).getFriends().size() != 0;
    }

}
