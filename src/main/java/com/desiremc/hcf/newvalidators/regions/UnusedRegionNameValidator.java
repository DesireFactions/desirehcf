package com.desiremc.hcf.newvalidators.regions;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class UnusedRegionNameValidator implements Validator<String>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, String arg)
    {
        Region region = RegionHandler.getRegion(arg);

        if (region != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "region.used_name");
            return false;
        }

        return true;
    }

}
