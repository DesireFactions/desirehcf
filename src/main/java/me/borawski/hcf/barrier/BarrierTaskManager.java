package me.borawski.hcf.barrier;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.borawski.hcf.Core;

public class BarrierTaskManager {

    private static BukkitTask barrierTask;

    public static void runBarrierTask() {
        if (barrierTask != null) {
            barrierTask.cancel();
        }
        barrierTask = Bukkit.getScheduler().runTaskTimer(Core.getInstance(), new BarrierTask(),
                Core.getConfigHandler().getInteger("barrier.refresh.ticks"),
                Core.getConfigHandler().getInteger("barrier.refresh.ticks"));
    }

}
