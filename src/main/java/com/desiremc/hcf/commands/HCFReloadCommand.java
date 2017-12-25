package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

import java.util.List;

public class HCFReloadCommand extends ValidCommand
{

    public HCFReloadCommand()
    {
        super("hcfreload", "Reload the lang file.", Rank.ADMIN, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        DesireHCF.getLangHandler().reload();
        DesireHCF.getConfigHandler().reload();
        DesireHCF.getLangHandler().sendRenderMessage(sender, "hcfreload", true, false);
    }

}
