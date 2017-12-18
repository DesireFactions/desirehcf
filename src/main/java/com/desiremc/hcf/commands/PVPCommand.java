package com.desiremc.hcf.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.PlayerHasSafeTimeLeft;

public class PVPCommand extends FactionValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new PlayerHasSafeTimeLeft());
    }

    @Override
    public void validFactionRun(FSession sender, String label[], List<CommandArgument<?>> args)
    {
        sender.setSafeTimeLeft(0);
        sender.save();
        DesireHCF.getLangHandler().sendString(sender.getPlayer(), "pvp.disabled");
        EntryRegistry.getInstance().removeValue(sender.getPlayer(), DesireHCF.getLangHandler().getStringNoPrefix("pvp.scoreboard"));
    }

}
