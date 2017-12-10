package com.desiremc.hcf.parser;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FactionSession;
import com.desiremc.hcf.session.FactionSessionHandler;
import org.bukkit.command.CommandSender;

public class FactionSessionParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        FactionSession session = FactionSessionHandler.getFactionSession(arg);

        if (session == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "faction_null");
            return null;
        }
        return FactionSessionHandler.getFactionSession(arg);
    }
}
