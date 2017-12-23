package com.desiremc.hcf.commands.factions;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.StringLengthValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.events.faction.FactionCreateEvent;
import com.desiremc.hcf.parsers.FactionTypeParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionType;
import com.desiremc.hcf.validators.SenderHasNoFactionValidator;

public class FactionCreateCommand extends FactionValidCommand
{

    public FactionCreateCommand()
    {
        super("create", "Crate a new faction.", true, new String[] { "new", "start" });

        addSenderValidator(new SenderHasNoFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new StringLengthValidator(DesireHCF.getConfigHandler().getInteger("factions.names.length.min"), DesireHCF.getConfigHandler().getInteger("factions.names.length.max")))
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(FactionType.class)
                .setName("type")
                .setParser(new FactionTypeParser())
                .setOptional()
                .setRequiredRank(Rank.ADMIN)
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        String name = (String) arguments.get(0).getValue();
        FactionType type = arguments.get(1).hasValue() ? (FactionType) arguments.get(1).getValue() : FactionType.PLAYER;

        Faction faction = FactionHandler.createFaction(sender, name, type);

        FactionCreateEvent event = new FactionCreateEvent(faction, sender);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
        {
            FactionHandler.deleteFaction(faction);
        }
        else
        {
            String broadcast = DesireHCF.getLangHandler().renderMessage("factions.create",
                    "{player}", sender.getName(),
                    "{faction}", faction.getName());

            Bukkit.broadcastMessage(broadcast);
        }
    }

}
