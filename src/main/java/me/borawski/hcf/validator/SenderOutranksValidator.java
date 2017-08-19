package me.borawski.hcf.validator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.SessionUtils;

public class SenderOutranksValidator extends CommandValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        Rank senderRank = SessionUtils.getRank(SessionHandler.getSession((Player) sender));
        Rank targetRank = SessionUtils.getRank((Session) arg);

        return senderRank.compareTo(targetRank) > 0;
    }

}
