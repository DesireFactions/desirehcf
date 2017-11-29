package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;

public class KitParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        HKit kit = HKitHandler.getKit(arg);
        
        if (kit == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kit.not_found");
        }
        
        return kit;
    }

}
