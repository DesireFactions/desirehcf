package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.validators.PlayerHasSafeTimeLeft;

import java.util.List;

public class PVPCommand extends ValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new PlayerHasSafeTimeLeft());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        FSession session = FSessionHandler.getFSession(sender.getUniqueId());

        session.setSafeTimeLeft(0);
        session.save();
        DesireHCF.getLangHandler().sendString(sender.getPlayer(), "pvp.disabled");
        EntryRegistry.getInstance().removeValue(session.getPlayer(), DesireHCF.getLangHandler().getStringNoPrefix("pvp.scoreboard"));
    }

}
