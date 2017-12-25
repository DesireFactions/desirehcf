package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.events.PlayerBlockMoveEvent;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.events.faction.FactionLeaveEvent;
import com.desiremc.hcf.listener.factions.ConnectionListener;
import com.desiremc.hcf.listener.factions.FactionListener;
import com.desiremc.hcf.listener.factions.PlayerListener;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionRelationship;
import com.desiremc.hcf.util.FactionsUtils;
import com.desiremc.hcf.validators.SenderHasFactionHomeValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FactionHomeCommand extends FactionValidCommand
{

    private static Map<UUID, BukkitTask> teleporting = new HashMap<>();

    public FactionHomeCommand()
    {
        super("home", "Teleport to the faction home.", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderHasFactionHomeValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        int time;
        Location loc = sender.getLocation();
        Faction currentFaction = FactionsUtils.getFaction(loc);
        if (RegionHandler.getRegion(loc) != null || currentFaction.isSafeZone())
        {
            time = 0;
        }
        else if (loc.getWorld().getEnvironment() == Environment.NETHER || currentFaction.isWilderness())
        {
            time = 20;
        }
        else if (loc.getWorld().getEnvironment() == Environment.THE_END)
        {
            time = -1;
        }
        else if (currentFaction.isWilderness())
        {
            time = 20;
        }
        else
        {
            FactionRelationship rel = currentFaction.getRelationshipTo(currentFaction);
            if (rel == FactionRelationship.ALLY || rel == FactionRelationship.MEMBER)
            {
                time = 10;
            }
            else
            {
                time = -1;
            }
            if (currentFaction.isRaidable())
            {
                time += 5;
            }
        }

        teleporting.put(sender.getUniqueId(), Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                if (sender.isOnline())
                {
                    sender.getPlayer().teleport(sender.getFaction().getHomeLocation());
                    DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.teleported", true, false);
                }
                teleporting.remove(sender.getUniqueId());
            }
        }, time * 20));

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.home.confirm", true, false,
                "{time}", time);
    }

    /**
     * Used to check if the player has clicked their ruby slippers together three times. This is used in ____ to check
     * if it is cancelled.<br>
     * <br>
     * The task needs to be cancelled in the following situations:
     * <ul>
     * <li>The player moves ({@link PlayerListener#onBlockMove(PlayerBlockMoveEvent)})</li>
     * <li>The player logs out ({@link ConnectionListener#onQuit(PlayerQuitEvent)})</li>
     * <li>The player leaves their faction ({@link FactionListener#onFactionLeave(FactionLeaveEvent)})</li>
     * </ul>
     * 
     * @return {@code true} if the player is going home.
     */
    public static BukkitTask getTeleportTask(UUID uuid)
    {
        return teleporting.remove(uuid);
    }

}
