package com.desiremc.hcf.validators.regions;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.RegionHandler;

public class RegionsExistValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (RegionHandler.getRegions().size() < 1)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "region.none");
            return false;
        }

        return true;
    }

}
