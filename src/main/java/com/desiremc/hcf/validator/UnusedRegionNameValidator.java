package com.desiremc.hcf.validator;

import com.desiremc.hcf.HCFCore;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
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
            HCFCore.getLangHandler().sendString(sender, "region.doesnt_exist");
            return false;
        }

        return true;
    }

}
