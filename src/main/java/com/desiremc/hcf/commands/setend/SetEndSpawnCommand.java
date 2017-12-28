package com.desiremc.hcf.commands.setend;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.api.SetEndAPI;

import java.util.List;

public class SetEndSpawnCommand extends ValidCommand
{

    public SetEndSpawnCommand()
    {
        super("spawn", "set end spawn", Rank.ADMIN, true, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        SetEndAPI.setEndSpawn(sender.getPlayer(), "endspawn", "set_end.spawn");
    }

}
