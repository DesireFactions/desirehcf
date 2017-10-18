package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;

public class PVPCommand extends ValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, new String[]{});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession s = HCFSessionHandler.getHCFSession(sender);

        s.setSafeTimeLeft(0);
        DesireHCF.getLangHandler().sendString(sender, "pvp.disabled");
    }

}
