package com.desiremc.hcf.tasks;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.SOTWHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

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
        for (Player p : Bukkit.getOnlinePlayers())
        {
            EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().getStringNoPrefix("sotw.scoreboard"), DateUtils.formatDateDiff(((start + (TIMER * 1000)) - System.currentTimeMillis()) / 1000));
        }

        if (getRemainingSeconds() <= 0)
        {
            cancel();
        }
    }

    private int getRemainingSeconds()
    {
        long currentTime = System.currentTimeMillis();
        return (start + (TIMER * 1000)) > currentTime ? NumberConversions.ceil(((start + (TIMER * 1000)) - currentTime) / 1000D) : 0;
    }

    public void cancel()
    {
        super.cancel();

        SOTWHandler.setSOTW(false);
        Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("sotw.over"));

        for (Player p : Bukkit.getOnlinePlayers())
        {
            EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().getStringNoPrefix("sotw.scoreboard"));
        }
    }
}
