package com.desiremc.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.utils.DateUtils;

public class ConnectionListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e)
    {
        HCFSessionHandler.initializeHCFSession(e.getPlayer().getUniqueId(), true);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        HCFSession s = HCFSessionHandler.initializeHCFSession(event.getPlayer().getUniqueId(), false);
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
