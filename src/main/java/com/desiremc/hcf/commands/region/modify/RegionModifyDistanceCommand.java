package com.desiremc.hcf.commands.region.modify;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.NumberSizeValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.RegionParser;
import com.desiremc.hcf.session.Region;

public class RegionModifyDistanceCommand extends ValidCommand
{

    public RegionModifyDistanceCommand()
    {
        super("distance", "Change the view distance of a region.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Region.class)
                .setName("region")
                .setParser(new RegionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("distnace")
                .setParser(new PositiveIntegerParser())
                .addValidator(new NumberSizeValidator<>(1, DesireHCF.getConfigHandler().getInteger("barrier.max-distance")))
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Region region = (Region) arguments.get(0).getValue();
        int distance = (Integer) arguments.get(1).getValue();

        int oldDistance = region.getViewDistance();

        region.setViewDistance(distance);
        region.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.change", true, false,
                "{change}", "distance",
                "{region}", region.getName(),
                "{old}", oldDistance,
                "{new}", distance);

    }

}
