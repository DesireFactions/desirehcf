package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.PlayerHasSafeTimeLeft;

import java.util.List;

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
        DesireHCF.getLangHandler().sendRenderMessage(sender.getPlayer(), "pvp.disabled", true, false);
        EntryRegistry.getInstance().removeValue(sender.getPlayer(), DesireHCF.getLangHandler().renderMessage("pvp.scoreboard", true));
    }

}
