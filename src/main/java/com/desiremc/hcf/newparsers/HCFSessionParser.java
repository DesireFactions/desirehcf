package com.desiremc.hcf.newparsers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;

public class HCFSessionParser implements Parser<HCFSession>
{

    @Override
    public HCFSession parseArgument(Session sender, String[] label, String rawArgument)
    {
        HCFSession arg = null;

        // try to find an online player by name
        Player p = Bukkit.getPlayerExact(rawArgument);

        // if the player is not online, try to search the database by name
        if (p == null)
        {
            arg = HCFSessionHandler.findOfflinePlayerByName(rawArgument);
        }
        // if they are online, just grab their session from the handler
        else
        {
            arg = HCFSessionHandler.getHCFSession(p.getUniqueId());
        }

        // if they were not found, send the error message and return null.
        if (arg == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "player-not-found");
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
