package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.listener.PickupListener;

import java.util.List;

public class CobbleCommand extends ValidCommand
{

    public CobbleCommand()
    {
        super("cobble", "Disable picking up cobble.", Rank.GUEST, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        if (PickupListener.toggleCobble(sender.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "cobble.disable", true, false);
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "cobble.enable", true, false);
        }
    }

}
