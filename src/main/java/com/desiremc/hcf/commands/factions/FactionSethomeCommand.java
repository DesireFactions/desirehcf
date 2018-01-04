package com.desiremc.hcf.commands.factions;

import java.util.List;

import org.bukkit.Location;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.SenderLandIsOwnValidator;

public class FactionSethomeCommand extends FactionValidCommand
{

    public FactionSethomeCommand()
    {
        super("sethome", "Sets the faction home.", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());
        addSenderValidator(new SenderLandIsOwnValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();

        Location location = sender.getPlayer().getLocation();

        faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.home.set", false, false,
                "{player}", sender.getName(),
                "{x}", location.getBlockX(),
                "{y}", location.getBlockY(),
                "{z}", location.getBlockZ()));
        faction.setHomeLocation(location);
        faction.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.home.set", true, false,
                "{player}", sender.getName(),
                "{x}", location.getBlockX(),
                "{y}", location.getBlockY(),
                "{z}", location.getBlockZ()));
    }

}
