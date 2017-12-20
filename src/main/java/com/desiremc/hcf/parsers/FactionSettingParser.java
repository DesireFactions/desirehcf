package com.desiremc.hcf.parsers;

import java.util.LinkedList;
import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.faction.FactionSetting;

public class FactionSettingParser implements Parser<FactionSetting>
{

    @Override
    public FactionSetting parseArgument(Session sender, String[] label, String rawArgument)
    {
        FactionSetting factionSetting = FactionSetting.getFactionSetting(rawArgument);
        if (factionSetting == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.setting.not_found");
            return null;
        }
        return factionSetting;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> settings = new LinkedList<>();
        for (FactionSetting factionSetting : FactionSetting.values())
        {
            settings.add(factionSetting.name().toLowerCase());
        }
        Parser.pruneSuggestions(settings, lastWord);
        return settings;
    }

}
