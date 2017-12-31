package com.desiremc.hcf.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.core.scoreboard.EntryRegistry;
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
    }

    @Override
    public void run()
    {
        //EntryRegistry.getInstance().setAll(DesireHCF.getLangHandler().renderMessage("sotw.scoreboard", false, false), DateUtils.formatDateDiff(getRemainingSeconds() + System.currentTimeMillis()));
        EntryRegistry.getInstance().setAll(DesireHCF.getLangHandler().renderMessage("sotw.scoreboard", false, false), "Test");
        if (getRemainingSeconds() <= 0)
        {
            cancel();
        }
    }

    private int getRemainingSeconds()
    {
        return (int) (((TIMER * 1000) + start) - System.currentTimeMillis());
    }

    public void cancel()
    {
        SOTWHandler.setSOTW(false);
        Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("sotw.over", true, false));

        EntryRegistry.getInstance().removeAll(DesireHCF.getLangHandler().renderMessage("sotw.scoreboard", false, false));
    }
}
