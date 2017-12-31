package com.desiremc.hcf.commands.kit;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
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

        StringBuilder sb = new StringBuilder();
        sb.append("§b");
        for (HKit kit : HKitHandler.getKits())
        {
            sb.append(kit.getName());
            sb.append("§7, §b");
        }
        sb.setLength(sb.length() - 6);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.list", true, false, "{kits}", sb.toString());
    }
}
