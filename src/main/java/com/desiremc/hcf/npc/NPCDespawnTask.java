package com.desiremc.hcf.npc;

import com.desiremc.hcf.DesireHCF;

public class NPCDespawnTask implements Runnable
{

    private final DesireHCF plugin;

    private final NPC npc;

    private long time;

    private int taskId;

    public NPCDespawnTask(DesireHCF plugin, NPC npc, long time)
    {
        this.plugin = plugin;
        this.npc = npc;
        this.time = time;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public NPC getNpc()
    {
        return npc;
    }

    public void start()
    {
        taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 1, 1).getTaskId();
    }

    public void stop()
    {
        plugin.getServer().getScheduler().cancelTask(taskId);
    }

    @Override
    public void run()
    {
        if (time > System.currentTimeMillis())
        {
            return;
        }

        NPCManager.despawn(npc);
    }

}
