package com.desiremc.hcf.barrier;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.desiremc.hcf.DesireCore;

public class BarrierTaskManager
{

    private static BukkitTask barrierTask;

    public static void runBarrierTask()
    {
        if (barrierTask != null)
        {
            barrierTask.cancel();
        }
        barrierTask = Bukkit.getScheduler().runTaskTimer(DesireCore.getInstance(), new BarrierTask(),
                DesireCore.getConfigHandler().getInteger("barrier.refresh.ticks"),
                DesireCore.getConfigHandler().getInteger("barrier.refresh.ticks"));
    }

}
