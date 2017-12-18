package com.desiremc.hcf.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class ConnectionListener implements Listener
{

    private static final boolean DEBUG = false;

    public static List<UUID> firstJoin = new ArrayList<>();

    private FileHandler config = DesireHCF.getConfigHandler();

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e)
    {
        if (DEBUG)
        {
            System.out.println("ConnectionListener.onJoin() event fired.");
        }
        Player p = e.getPlayer();
        FSession session = FSessionHandler.initializeFSession(e.getPlayer().getUniqueId());
        boolean safe = false;
        for (Region region : RegionHandler.getRegions())
        {
            if (region.getWorld() == p.getLocation().getWorld() && region.getRegionBlocks().isWithin(p.getLocation()))
            {
                safe = true;
            }
        }
        // TODO Look into a better solution for isOnGround.
        if (safe && p.isOnGround())
        {
            TagHandler.setLastValidLocation(p.getUniqueId(), p.getLocation());
        }

        if (session.getSafeTimeLeft() > 0)
        {
            if (DEBUG)
            {
                System.out.println("ConnectionListener.onJoin() safe time > 0.");
            }
            if (safe)
            {
                session.getSafeTimer().setScoreboard();
            }
            else
            {
                session.getSafeTimer().resume();
            }
        }

        if (firstJoin.contains(p.getUniqueId()))
        {
            Location loc = new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"), (float) config.getDouble("spawn.yaw").doubleValue(), (float) config.getDouble("spawn.pitch").doubleValue());

            p.teleport(loc);
            firstJoin.remove(p.getUniqueId());
        }

        Session s = SessionHandler.getSession(session.getPlayer());
        if (!s.hasAchievement(Achievement.FIRST_LOGIN))
        {
            s.awardAchievement(Achievement.FIRST_LOGIN, true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        FSession session = FSessionHandler.getOnlineFSession(e.getPlayer().getUniqueId());

        session.getSafeTimer().pause();

        FSessionHandler.getInstance().save(session);
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event)
    {
        FSession s;
        try
        {
            s = FSessionHandler.getOnlineFSession(event.getUniqueId());
        }
        catch (Exception ex)
        {
            // if the person trying to join has never connected before, this will error out.
            // The try catch fixes that problem until I figure out if there is a nicer way
            // to handle it.
            return;
        }
        DeathBan ban = s.getActiveDeathBan();
        if (ban != null)
        {
            String reason = DesireCore.getLangHandler().getPrefix() + "\nYou have an active death ban on this server. It ends in " + DateUtils.formatDateDiff(ban.getStartTime() + s.getRank().getDeathBanTime());
            if (ban.getStartTime() + s.getRank().getDeathBanTime() - System.currentTimeMillis() > 3_600_00)
            {
                reason = reason.replaceAll(" ([0-9]{1,2}) (seconds|second)", "");
            }
            event.disallow(Result.KICK_BANNED, reason);
        }
    }

}
