package com.desiremc.hcf.validator;

import com.desiremc.hcf.session.Session;
import org.bukkit.command.CommandSender;

public class PlayerIsBannedValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        boolean first = super.validateArgument(sender, label, arg);
        if (!first)
        {
            return false;
        }

        Session session = (Session) arg;

        if (session.isBanned() != null)
        {
            LANG.sendString(sender, "not_banned");
            return false;
        }

        return true;
    }

}
