package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionParser implements ArgumentParser
{

    private static final LangHandler LANG = DesireHCF.getLangHandler();

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        Region r = RegionHandler.getInstance().getRegion(arg);
        if (r == null)
        {
            LANG.sendString(sender, "region.not_found");
            return null;
        }
        return r;
    }

}
