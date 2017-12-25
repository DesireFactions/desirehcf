package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderFactionLeaderValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class FactionDisbandCommand extends FactionValidCommand
{

    private Cache<UUID, Long> disbanding;

    public FactionDisbandCommand()
    {
        super("disband", "Disbands your faction.");
        disbanding = new Cache<>(100, DesireHCF.getInstance());

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionLeaderValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .setRequiredRank(Rank.ADMIN)
                .setOptional()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();

        if (!disbanding.containsKey(sender.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.disband.confirm", true, false);
            disbanding.put(sender.getUniqueId(), System.currentTimeMillis());
        }
        else
        {
            disbanding.remove(sender.getUniqueId());
            FactionHandler.deleteFaction(faction);
            Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("factions.disband.broadcast", true, false,
                    "{faction}", faction.getName()));
        }
    }

}
