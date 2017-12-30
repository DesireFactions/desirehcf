package com.desiremc.hcf.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.SOTWHandler;

public class SOTWTask extends BukkitRunnable
{
    private int TIMER;
    private long start;

    public SOTWTask()
    {
        TIMER = DesireHCF.getConfigHandler().getInteger("sotw.time");
        start = System.currentTimeMillis();

        run();
    }

    @Override
    public void run()
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().renderMessage("sotw.scoreboard", false, false), DateUtils.formatDateDiff(getRemainingSeconds()));
        }

        if (getRemainingSeconds() <= 0)
        {
            cancel();
            return;
        }
        
        TIMER++;

        runTaskLater(DesireHCF.getInstance(), 20L);
    }

    private int getRemainingSeconds()
    {
        return (int) (((TIMER * 1000) + start) - System.currentTimeMillis());
    }

    public void cancel()
    {
        SOTWHandler.setSOTW(false);
        Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("sotw.over", true, false));

        for (Player p : Bukkit.getOnlinePlayers())
        {
            EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().renderMessage("sotw.scoreboard", false, false));
        }
    }
}
