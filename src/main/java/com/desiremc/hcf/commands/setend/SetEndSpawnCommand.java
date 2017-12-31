package com.desiremc.hcf.commands.setend;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;

public class SetEndSpawnCommand extends ValidCommand
{

    public SetEndSpawnCommand()
    {
        super("spawn", "set end spawn", Rank.ADMIN, true, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        DesireHCF.getLangHandler().sendRenderMessage(sender, "set_end.spawn", true, false);

        DesireHCF.getConfigHandler().setString("endspawn", BukkitUtils.toString(sender.getPlayer().getLocation()));
    }

}
