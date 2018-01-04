package com.desiremc.hcf.validators;

import org.bukkit.Location;
import org.bukkit.World;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionRelationship;

/**
 * This validator assumes that the sender has a faction. If they do not, it will fail gracefully but not send an error
 * message.
 *
 * @author Michael Ziluck
 */
public class SenderCanFactionHome extends FactionSenderValidator
{
    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (sender.getFaction().getHomeLocation() == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.not_set", true, false);
            return false;
        }

        Location loc = sender.getLocation();
        Faction faction = FactionHandler.getFaction(loc);

        if (loc.getWorld().getEnvironment() == World.Environment.THE_END)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.invalid", true, false);
            return false;
        }

        FactionRelationship rel = sender.getFaction().getRelationshipTo(faction);

        if (faction.isNormal() && rel != FactionRelationship.ALLY)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.invalid", true, false);
            return false;
        }

        if (TagHandler.getTaggedPlayers().contains(sender.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.pvp", true, false);
            return false;
        }

        if (sender.getSafeTimeLeft() > 0)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.safe", true, false);
            return false;
        }

        return true;
    }

}
