package com.desiremc.hcf.commands.region.modify;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.newvalidators.StringLengthValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.newparsers.RegionParser;
import com.desiremc.hcf.newvalidators.regions.UnusedRegionNameValidator;
import com.desiremc.hcf.session.Region;

public class RegionModifyNameCommand extends ValidCommand
{

    public RegionModifyNameCommand()
    {
        super("name", "Change the name of a region.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Region.class)
                .setName("region")
                .setParser(new RegionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new UnusedRegionNameValidator())
                .addValidator(new StringLengthValidator(1, DesireHCF.getConfigHandler().getInteger("regions.max-name")))
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Region region = (Region) arguments.get(0).getValue();
        String name = (String) arguments.get(1).getValue();
        String oldName = region.getName();

        region.setName(name);
        region.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.change",
                "{change}", "name",
                "{region}", region.getName(),
                "{old}", oldName,
                "{new}", name);

    }

}
