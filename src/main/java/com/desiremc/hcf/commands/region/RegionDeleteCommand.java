package com.desiremc.hcf.commands.region;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.RegionParser;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

import java.util.List;

public class RegionDeleteCommand extends ValidCommand
{

    public RegionDeleteCommand()
    {
        super("delete", "Delete a protected region.", Rank.ADMIN, new String[] { "remove" });

        addArgument(CommandArgumentBuilder.createBuilder(Region.class)
                .setName("region")
                .setParser(new RegionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Region region = (Region) arguments.get(0).getValue();
        RegionHandler.getInstance().delete(region);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.delete", "{name}", region.getName());
    }

}
