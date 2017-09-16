package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;

public class PVPCommand extends ValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, new String[]{});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session s = SessionHandler.getSession(sender);

        s.setSafeTimeLeft(0);
        LANG.sendString(sender, "pvp.disabled");
    }

}
