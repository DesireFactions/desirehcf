package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class UnusedRegionNameValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Region r = RegionHandler.getInstance().getRegion((String) arg);
        if (r == null)
        {
            DesireCore.getLangHandler().sendString(sender, "region.used_name");
            return false;
        }
        
        return true;
    }

}
