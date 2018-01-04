package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

public class IllegalWordValidator implements Validator<String>
{

    public static String[] illegalWords = {"fuck", "ass", "bitch"};

    @Override
    public boolean validateArgument(Session sender, String[] label, String arg)
    {
        for (String word : illegalWords)
        {
            if (arg.contains(word))
            {
                DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.name.not_allowed", true, false, "{word}", word);
                return false;
            }
        }
        return true;
    }
}
