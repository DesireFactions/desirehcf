package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
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
        HCFSession session = HCFSessionHandler.getHCFSession(sender.getUniqueId());

        session.setSafeTimeLeft(0);
        session.save();
        DesireHCF.getLangHandler().sendString(sender.getPlayer(), "pvp.disabled");
        EntryRegistry.getInstance().removeValue(session.getPlayer(), DesireHCF.getLangHandler().getStringNoPrefix("pvp.scoreboard"));
    }

}
