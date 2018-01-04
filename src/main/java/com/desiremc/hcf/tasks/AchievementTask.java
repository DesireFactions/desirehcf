package com.desiremc.hcf.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class AchievementTask extends BukkitRunnable
{

    @Override
    public void run()
    {
        for (Session session : SessionHandler.getOnlineSessions())
        {
            if (session.getTotalPlayed() >= 360000000)
            {
                session.awardAchievement(Achievement.PLAYTIME_100, true);
            }
            else if (session.getTotalPlayed() >= 172800000)
            {
                session.awardAchievement(Achievement.PLAYTIME_48, true);
            }
            else if (session.getTotalPlayed() >= 86400000)
            {
                session.awardAchievement(Achievement.PLAYTIME_24, true);
            }
        }
    }

}
