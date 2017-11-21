package com.desiremc.hcf.listener;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
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

        if (firstJoin.contains(p.getUniqueId()))
        {
            Location loc = new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"), (float) config.getDouble("spawn.yaw").doubleValue(), (float) config.getDouble("spawn.pitch").doubleValue());

            p.teleport(loc);
            firstJoin.remove(p.getUniqueId());
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
