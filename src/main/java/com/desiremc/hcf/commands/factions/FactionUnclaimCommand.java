package com.desiremc.hcf.commands.factions;

import java.util.List;

import org.bukkit.Location;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderCanUnclaimLocation;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.SenderLandIsOwn;

public class FactionUnclaimCommand extends FactionValidCommand
{
    protected FactionUnclaimCommand()
    {
        super("unclaim", "Unclaim the land your're standing in", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());
        addSenderValidator(new SenderLandIsOwn());
        addSenderValidator(new SenderCanUnclaimLocation());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        Location location = sender.getLocation();

        BoundedArea claim = FactionHandler.getArea(location);

        faction.addLog(DesireHCF.getLangHandler().renderMessage("factions.unclaim.success", false, false, "{player}", sender.getName()));
        faction.removeClaim(claim);
        faction.save();

        FactionHandler.removeClaim(faction, claim);

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.unclaim.success", true, false, "{player}", sender.getName()));

    }
}
