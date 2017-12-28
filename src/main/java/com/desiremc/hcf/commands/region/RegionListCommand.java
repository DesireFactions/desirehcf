package com.desiremc.hcf.commands.region;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.validators.regions.RegionsExistValidator;

import java.util.Iterator;
import java.util.List;

public class RegionListCommand extends ValidCommand
{

    public RegionListCommand()
    {
        super("list", "List all the regions created.", Rank.ADMIN);

        addSenderValidator(new RegionsExistValidator());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = RegionHandler.getRegionNames().iterator();

        while (it.hasNext())
        {
            sb.append(it.next());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.list", true, false,
                "{regions}", sb.toString());
    }

}
