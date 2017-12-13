package com.desiremc.hcf.newparsers;

import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionParser implements Parser<Region>
{

    @Override
    public Region parseArgument(Session sender, String[] label, String rawArgument)
    {
        Region region = RegionHandler.getRegion(rawArgument);
        if (region == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "region.not_found");
            return null;
        }
        return region;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> regions = RegionHandler.getRegionNames();
        Parser.pruneSuggestions(regions, lastWord);
        return regions;
    }

}
