package me.borawski.hcf.validator;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.session.SessionHandler;

public class SenderIncommingFriendRequestsValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        return SessionHandler.getSession(sender).getIncomingFriendRequests().size() > 0;
    }

}
