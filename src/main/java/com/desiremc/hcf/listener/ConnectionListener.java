package com.desiremc.hcf.listener;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectionListener implements Listener
{

    public static List<UUID> firstJoin = new ArrayList<>();

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        FSessionHandler.initializeFSession(e.getPlayer().getUniqueId());
        FSession fSession = FSessionHandler.getOnlineFSession(e.getPlayer().getUniqueId());
        boolean safe = false;
        for (Region region : RegionHandler.getRegions())
        {
            if (region.getWorld() == player.getLocation().getWorld() && region.getRegionBlocks().isWithin(player.getLocation()))
            {
                safe = true;
            }
        }
        // TODO Look into a better solution for isOnGround.
        if (safe && player.isOnGround())
        {
            TagHandler.setLastValidLocation(player.getUniqueId(), player.getLocation());
        }

        if (fSession.getSafeTimeLeft() > 0)
        {
            if (safe)
            {
                fSession.getSafeTimer().setScoreboard();
            }
            else
            {
                fSession.getSafeTimer().resume();
            }
        }

        if (firstJoin.contains(player.getUniqueId()))
        {
            FileHandler config = DesireCore.getConfigHandler();
            Location loc = new Location(Bukkit.getWorld(config.getString("spawn.world")),
                    config.getDouble("spawn.x"),
                    config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"),
                    (float) config.getDouble("spawn.yaw").doubleValue(),
                    (float) config.getDouble("spawn.pitch").doubleValue());

            player.teleport(loc);
            firstJoin.remove(player.getUniqueId());
        }

        Session s = SessionHandler.getSession(fSession.getSender());
        if (!s.hasAchievement(Achievement.FIRST_LOGIN))
        {
            s.awardAchievement(Achievement.FIRST_LOGIN, true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent e)
    {
        FSession fSession = FSessionHandler.getOnlineFSession(e.getPlayer().getUniqueId());

        fSession.getSafeTimer().pause();
        fSession.save();

        FSessionHandler.endSession(fSession);
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event)
    {
        FSession fSession;
        try
        {
            fSession = FSessionHandler.getGeneralFSession(event.getUniqueId());
        }
        catch (Exception ex)
        {
            // if the person trying to join has never connected before, this will error out.
            // The try catch fixes that problem until I figure out if there is a nicer way
            // to handle it.
            return;
        }
        DeathBan ban = fSession.getActiveDeathBan();
        if (ban != null)
        {
            String reason = DesireCore.getLangHandler().getPrefix() + "\nYou have an active death ban on this server. It ends in " + DateUtils.formatDateDiff(ban.getStartTime() + fSession.getRank().getDeathBanTime());
            if (ban.getStartTime() + fSession.getRank().getDeathBanTime() - System.currentTimeMillis() > 3_600_00)
            {
                reason = reason.replaceAll(" ([0-9]{1,2}) (seconds|second)", "");
            }
            event.disallow(Result.KICK_BANNED, reason);
        }
    }

}
