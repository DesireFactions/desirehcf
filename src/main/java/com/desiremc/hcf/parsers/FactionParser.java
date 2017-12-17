package com.desiremc.hcf.parsers;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;

import java.util.ArrayList;
import java.util.List;

public class FactionParser implements Parser<Faction>
{

    @Override
    public Faction parseArgument(Session sender, String[] label, String rawArgument)
    {
        Faction faction = FactionsUtils.getFaction(rawArgument);

        if (faction == null || faction.isWilderness())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.not_found");
            return null;
        }

        return faction;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> factions = new ArrayList<>(FactionsUtils.getFactionNames());
        Parser.pruneSuggestions(factions, lastWord);
        return factions;
    }

}
