package com.desiremc.hcf.commands.kit;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;

public class KitManagegmentListCommand extends ValidCommand
{
    public KitManagegmentListCommand()
    {
        super("list", "List all kits", Rank.ADMIN, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        for (HKit kit : HKitHandler.getKits())
        {
            sender.sendMessage(kit.getName());
        }
    }
}
