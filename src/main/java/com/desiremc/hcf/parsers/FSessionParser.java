package com.desiremc.hcf.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class FSessionParser implements Parser<FSession>
{

    @Override
    public FSession parseArgument(Session sender, String[] label, String rawArgument)
    {
        FSession arg = null;

        // try to find an online player by name
        Player p = Bukkit.getPlayerExact(rawArgument);

        // if the player is not online, try to search the database by name
        if (p == null)
        {
            arg = FSessionHandler.getFSessionByName(rawArgument);
        }
        // if they are online, just grab their session from the handler
        else
        {
            arg = FSessionHandler.getGeneralFSession(p.getUniqueId());
        }

        // if they were not found, send the error message and return null.
        if (arg == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "player-not-found", true, false);
            return null;
        }
        
        return arg;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
