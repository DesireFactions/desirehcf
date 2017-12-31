package com.desiremc.hcf.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.handler.SpawnHandler;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class ConnectionListener implements Listener
{

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

        if (SpawnHandler.getInstance().getPlayer(player))
        {
            Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    player.teleport(SpawnHandler.getInstance().getSpawn());
                }
            }, 20L);

            SpawnHandler.getInstance().removePlayer(player);
        }

        if (!fSession.hasAchievement(Achievement.FIRST_LOGIN))
        {
            fSession.awardAchievement(Achievement.FIRST_LOGIN, true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent e)
    {
        FSession fSession = FSessionHandler.getOnlineFSession(e.getPlayer().getUniqueId());

        fSession.getSafeTimer().pause();
        if (fSession.hasClaimSession())
        {
            fSession.getClaimSession().delete();
            fSession.getClaimSession().run();
        }
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
