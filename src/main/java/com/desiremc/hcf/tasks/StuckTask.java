package com.desiremc.hcf.tasks;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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
                    return FactionHandler.getFaction(column).isWilderness() || FactionHandler.getFaction(column).isSafeZone();
                }
            })
            {

                @Override
                public void onSuccess(BlockColumn column)
                {
                    Location playerLoc = fSession.getPlayer().getLocation();

                    Location loc = new Location(column.getWorld(), column.getX(), 0, column.getZ(), playerLoc.getYaw(), playerLoc.getPitch());

                    System.out.println("yay");
                    fSession.getPlayer().teleport(column.getSafeLocation(loc));

                    DesireHCF.getLangHandler().sendRenderMessage(fSession.getPlayer(), message, true, false);
                }

                @Override
                public void onOverflow()
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.stuck.overflow", true, false);
                }
            });

            EntryRegistry.getInstance().removeValue(fSession.getPlayer(),
                    DesireHCF.getLangHandler().renderMessage("factions.stuck.scoreboard", false, false));
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
