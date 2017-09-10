package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.session.SessionHandler;

public class SenderOutgoingFriendRequestsValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        return SessionHandler.getSession((Player) sender).getOutgoingFriendRequests().size() > 0;
    }

}
