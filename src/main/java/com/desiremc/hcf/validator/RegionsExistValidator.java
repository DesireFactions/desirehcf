package com.desiremc.hcf.validator;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class RegionsExistValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Collection<Region> regions = RegionHandler.getInstance().getRegions();
        
        if (regions.size() < 1)
        {
            HCFCore.getLangHandler().sendString(sender, "region.none");
            return false;
        }
        
        return true;
    }

}
