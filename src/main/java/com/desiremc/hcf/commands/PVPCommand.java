package com.desiremc.hcf.commands;

import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Rank;

public class PVPCommand extends ValidCommand
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, new String[]{});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession s = HCFSessionHandler.getHCFSession(sender);

        s.setSafeTimeLeft(0);
        LANG.sendString(sender, "pvp.disabled");
    }

}
