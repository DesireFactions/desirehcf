package com.desiremc.hcf.parser;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.parser.ArgumentParser;
import com.desiremc.hcf.util.DateUtils;

public class TimeParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        try
        {
            return DateUtils.parseDateDiff(arg, true);
        }
        catch (Exception e)
        {
            LANG.sendString(sender, "not_time");
            return null;
        }
    }

}
