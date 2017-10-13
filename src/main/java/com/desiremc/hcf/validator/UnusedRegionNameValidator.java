package com.desiremc.hcf.validator;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import org.bukkit.command.CommandSender;

public class UnusedRegionNameValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Region r = RegionHandler.getInstance().getRegion((String) arg);
        if (r == null)
        {
            HCFCore.getLangHandler().sendString(sender, "region.used_name");
            return false;
        }

        return true;
    }

}
