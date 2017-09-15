package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.desiremc.hcf.DesireCore;

public class MaterialDataParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        ItemStack is = DesireCore.getItemHandler().get(arg);
        if (is == null)
        {
            sender.sendMessage(LANG.getString("invalid_item"));
            return null;
        }
        
        return is.getData();
    }

}
