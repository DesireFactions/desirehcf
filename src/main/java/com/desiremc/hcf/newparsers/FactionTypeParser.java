package com.desiremc.hcf.newparsers;

import java.util.LinkedList;
import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.faction.FactionType;

public class FactionTypeParser implements Parser<FactionType>
{

    @Override
    public FactionType parseArgument(Session sender, String[] label, String rawArgument)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        lastWord = lastWord.toLowerCase();
        List<String> types = new LinkedList<>();
        for (FactionType type : FactionType.values())
        {
            types.add(type.name().toLowerCase());
        }
        Parser.pruneSuggestions(types, lastWord);
        return types;
    }

}
