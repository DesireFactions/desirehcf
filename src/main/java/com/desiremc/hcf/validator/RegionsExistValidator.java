package com.desiremc.hcf.validator;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionsExistValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Collection<Region> regions = RegionHandler.getInstance().getRegions();
        
        if (regions.size() < 1)
        {
            LANG.sendString(sender, "regions.none");
            return false;
        }
        
        return true;
    }

}
