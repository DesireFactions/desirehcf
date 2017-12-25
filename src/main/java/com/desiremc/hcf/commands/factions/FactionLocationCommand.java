package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import org.bukkit.Location;

import java.util.List;

public class FactionLocationCommand extends FactionValidCommand
{

    public FactionLocationCommand()
    {
        super("location", "Announce your location to all faction members.", true, new String[] {"coords", "loc"});

        addSenderValidator(new SenderHasFactionValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        Location loc = sender.getPlayer().getLocation();

        String message = DesireHCF.getLangHandler().renderMessage("factions.location", false, false,
                "{player}", sender.getName(),
                "{x}", Math.round(loc.getX()),
                "{y}", Math.round(loc.getY()),
                "{z}", Math.round(loc.getZ()),
                "{world}", loc.getWorld().getName());

        for (FSession online : faction.getOnlineMembers())
        {
            online.sendMessage(message);
        }
    }

}
