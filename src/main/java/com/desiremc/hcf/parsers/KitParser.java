package com.desiremc.hcf.parsers;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;

import java.util.ArrayList;
import java.util.List;

public class KitParser implements Parser<HKit>
{
    @Override
    public HKit parseArgument(Session sender, String[] label, String rawArgument)
    {
        HKit kit = HKitHandler.getKit(rawArgument);

        if (kit == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.not_found");
            return null;
        }

        return kit;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> kits = new ArrayList<>(HKitHandler.getKitNames());
        Parser.pruneSuggestions(kits, lastWord);
        return kits;
    }
}
