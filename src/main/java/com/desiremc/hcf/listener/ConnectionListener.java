package com.desiremc.hcf.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class ConnectionListener implements Listener
{

    private static final boolean DEBUG = false;

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e)
    {
        if (DEBUG)
        {
            System.out.println("ConnectionListener.onJoin() event fired.");
        }
        Player p = e.getPlayer();
        HCFSession session = HCFSessionHandler.initializeHCFSession(e.getPlayer().getUniqueId(), true);
        boolean safe = false;
        for (Region r : RegionHandler.getInstance().getRegions())
        {
            if (r.getWorld().equalsIgnoreCase(p.getLocation().getWorld().getName()) && r.getRegion().isWithin(p.getLocation()))
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
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(e.getPlayer().getUniqueId());

        HCFSessionHandler.getInstance().save(session);
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event)
    {
        HCFSession s = HCFSessionHandler.initializeHCFSession(event.getUniqueId(), false);
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
