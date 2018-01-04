package com.desiremc.hcf.parsers;

import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;

public class FactionStringParser implements Parser<String>
{

    @Override
    public String parseArgument(Session sender, String[] label, String argument)
    {
        StringBuilder builder = new StringBuilder();
        for (char character : argument.toCharArray())
        {
            if (character >= '0' && character <= '9'
                    || character >= 'A' && character <= 'Z'
                    || character >= 'a' && character <= 'z')
            {
                builder.append(character);
            }
        }
        return builder.toString();
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
