package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import org.bukkit.Location;

import java.util.List;

public class FactionSethomeCommand extends FactionValidCommand
{

    public FactionSethomeCommand()
    {
        super("sethome", "Sets the faction home.", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();

        Location location = sender.getPlayer().getLocation();

        faction.setHomeLocation(location);
        faction.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.home.set",
                "{player}", sender.getName(),
                "{x}", location.getBlockX(),
                "{y}", location.getBlockY(),
                "{z}", location.getBlockZ()));
    }

}
