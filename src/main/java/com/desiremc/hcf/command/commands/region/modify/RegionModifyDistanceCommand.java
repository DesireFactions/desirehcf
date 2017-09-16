package com.desiremc.hcf.command.commands.region.modify;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.IntegerParser;
import com.desiremc.hcf.parser.RegionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.validator.IntegerSizeValidator;

public class RegionModifyDistanceCommand extends ValidCommand
{

    public RegionModifyDistanceCommand()
    {
        super("distance", "Change the view distance of a region.", Rank.ADMIN, new String[] { "region", "distance" });

        addParser(new RegionParser(), "region");
        addParser(new IntegerParser(), "distance");

        addValidator(new IntegerSizeValidator(1, DesireCore.getConfigHandler().getInteger("barrier.max-distance")), "distance");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Region r = (Region) args[0];
        int distance = (Integer) args[1];
        int oldDistance = r.getViewDistance();
        r.setViewDistance(distance);
        RegionHandler.getInstance().save(r);
        
        LANG.sendRenderMessage(sender, "region.changed", "{change}", "distance", "{old}", String.valueOf(oldDistance), "{new}", String.valueOf(distance));

    }

}
