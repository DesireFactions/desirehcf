package com.desiremc.hcf.tasks;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionHandler;
import org.bukkit.Bukkit;

import java.util.function.Predicate;

public class StuckTask implements Runnable
{

    private FSession fSession;

    private String message;

    private BlockColumn column;

    private boolean cancelled;

    private int count = 0;

    private int max = DesireHCF.getConfigHandler().getInteger("factions.stuck.time");

    public StuckTask(FSession fSession, BlockColumn column, String message)
    {
        this.fSession = fSession;
        this.column = column;
        this.message = message;
    }

    @Override
    public void run()
    {
        EntryRegistry.getInstance().setValue(fSession.getPlayer(),
                DesireHCF.getLangHandler().renderMessage("factions.stuck.scoreboard", false, false),
                formattedTime());

        if (cancelled)
        {
            return;
        }
        count++;
        if (count >= max)
        {
            cancel();
            Bukkit.getScheduler().runTaskAsynchronously(DesireHCF.getInstance(), new SpiralBlockSearch(column, new Predicate<BlockColumn>()
            {

                @Override
                public boolean test(BlockColumn column)
                {
                    return FactionHandler.getFaction(column).isWilderness();
                }
            })
            {

                @Override
                public void onSuccess(BlockColumn column)
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession, message, true, false);
                }

                @Override
                public void onOverflow()
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.stuck.overflow", true, false);
                }
            });
        }
        else
        {
            Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), this, 20L);
        }
    }

    private String formattedTime()
    {
        int remaining = max - count;
        return (remaining / 60) + ":" + (remaining % 60);
    }

    public void cancel()
    {
        cancelled = true;
    }

}
