package com.desiremc.hcf.parser;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;

import org.bukkit.command.CommandSender;

public class FactionSessionParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        Faction session = FactionHandler.getFaction(arg);

        if (session == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.not_found");
            return null;
        }
        return FactionHandler.getFaction(arg);
    }
}
