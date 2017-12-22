package com.desiremc.hcf.parsers;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.faction.FactionChannel;

import java.util.LinkedList;
import java.util.List;

public class FactionChannelParser implements Parser<FactionChannel>
{

    @Override
    public FactionChannel parseArgument(Session sender, String[] label, String rawArgument)
    {
        if (rawArgument.equalsIgnoreCase("f") || rawArgument.equalsIgnoreCase("fac") || rawArgument.equalsIgnoreCase("faction"))
        {
            return FactionChannel.FACTION;
        }
        if (rawArgument.equalsIgnoreCase("g") || rawArgument.equalsIgnoreCase("gen") || rawArgument.equalsIgnoreCase("general"))
        {
            return FactionChannel.GENERAL;
        }
        if (rawArgument.equalsIgnoreCase("a") || rawArgument.equalsIgnoreCase("ally"))
        {
            return FactionChannel.ALLY;
        }
        return null;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        lastWord = lastWord.toLowerCase();
        List<String> types = new LinkedList<>();
        for (FactionChannel type : FactionChannel.values())
        {
            types.add(type.name().toLowerCase());
        }
        Parser.pruneSuggestions(types, lastWord);
        return types;
    }

}
