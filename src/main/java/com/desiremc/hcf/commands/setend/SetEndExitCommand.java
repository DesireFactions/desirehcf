package com.desiremc.hcf.commands.setend;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;

public class SetEndExitCommand extends ValidCommand
{

    public SetEndExitCommand()
    {
        super("exit", "set end exit", Rank.ADMIN, true, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        DesireHCF.getLangHandler().sendRenderMessage(sender, "set_end.exit", true, false);

        DesireHCF.getConfigHandler().setString("endexit", BukkitUtils.toString(sender.getPlayer().getLocation()));
    }
}
