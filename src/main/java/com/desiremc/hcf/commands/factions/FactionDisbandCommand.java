package com.desiremc.hcf.commands.factions;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;

public class FactionDisbandCommand extends FactionValidCommand
{

    private Cache<UUID, Long> disbanding;

    public FactionDisbandCommand(String name, String description)
    {
        super("disband", "Disbands your faction.");
        disbanding = new Cache<>(100, DesireHCF.getInstance());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        if (!disbanding.containsKey(sender.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.disband.confirm");
            disbanding.put(sender.getUniqueId(), System.currentTimeMillis());
        }
        else
        {
            disbanding.remove(sender.getUniqueId());
            FactionHandler.deleteFaction(faction);
            Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("factions.disband.broadcast",
                    "{faction}", faction.getName()));
        }
    }

}
