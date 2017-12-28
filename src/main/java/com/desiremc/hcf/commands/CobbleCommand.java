package com.desiremc.hcf.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.hcf.DesireHCF;

public class CobbleCommand extends ValidCommand
{

    public CobbleCommand()
    {
        super("cobble", "Disable picking up cobble.", Rank.GUEST, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        if (sender.toggleSetting(SessionSetting.COBBLE))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "cobble.disable", true, false);
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "cobble.enable", true, false);
        }
        sender.save();
    }

}
