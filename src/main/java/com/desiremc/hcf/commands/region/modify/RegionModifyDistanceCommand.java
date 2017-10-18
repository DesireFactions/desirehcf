package com.desiremc.hcf.commands.region.modify;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.IntegerSizeValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.RegionParser;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionModifyDistanceCommand extends ValidCommand
{

    public RegionModifyDistanceCommand()
    {
        super("distance", "Change the view distance of a region.", Rank.ADMIN, new String[] { "region", "distance" });

        addParser(new RegionParser(), "region");
        addParser(new IntegerParser(), "distance");

        addValidator(new IntegerSizeValidator(1, DesireHCF.getConfigHandler().getInteger("barrier.max-distance")), "distance");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Region r = (Region) args[0];
        int distance = (Integer) args[1];
        int oldDistance = r.getViewDistance();
        r.setViewDistance(distance);
        RegionHandler.getInstance().save(r);
        
        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.changed", "{change}", "distance", "{old}", String.valueOf(oldDistance), "{new}", String.valueOf(distance));

    }

}
