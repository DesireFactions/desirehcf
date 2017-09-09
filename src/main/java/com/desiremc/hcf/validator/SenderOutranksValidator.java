package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.SessionUtils;

public class SenderOutranksValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        Rank senderRank = SessionUtils.getRank(SessionHandler.getSession((Player) sender));
        Rank targetRank = SessionUtils.getRank((Session) arg);

        return senderRank.compareTo(targetRank) > 0;
    }

}
