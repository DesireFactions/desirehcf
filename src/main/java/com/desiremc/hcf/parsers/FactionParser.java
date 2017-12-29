package com.desiremc.hcf.parsers;

import java.util.ArrayList;
import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;

public class FactionParser implements Parser<Faction>
{

    @Override
    public Faction parseArgument(Session sender, String[] label, String rawArgument)
    {
        Faction faction;

        if (FactionsUtils.getFaction(rawArgument) != null && (FactionsUtils.getFaction(rawArgument).isNormal() || sender.getRank().isManager()))
        {
            faction = FactionsUtils.getFaction(rawArgument);
        }
        else if (FSessionHandler.getFSessionByName(rawArgument) != null
                && FSessionHandler.getFSessionByName(rawArgument).getFaction() != null
                && !FSessionHandler.getFSessionByName(rawArgument).getFaction().isWilderness())
        {
            faction = FSessionHandler.getFSessionByName(rawArgument).getFaction();
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.not_found", true, false);
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
