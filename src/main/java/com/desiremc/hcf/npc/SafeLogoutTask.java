package com.desiremc.hcf.npc;

import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.NumberConversions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SafeLogoutTask extends BukkitRunnable
{

    private final static int TIMER = DesireHCF.getConfigHandler().getInteger("logout.time");

    private final static Map<UUID, SafeLogoutTask> tasks = new HashMap<>();

    private final DesireHCF plugin;

    private final UUID playerId;

    private final Location loc;

    private final long logoutTime;

    private int remainingSeconds = Integer.MAX_VALUE;

    private boolean finished;

    public SafeLogoutTask(DesireHCF plugin, Player player, long logoutTime)
    {
        this.plugin = plugin;
        this.playerId = player.getUniqueId();
        this.loc = player.getLocation();
        this.logoutTime = logoutTime;
    }

    private int getRemainingSeconds()
    {
        long currentTime = System.currentTimeMillis();
        return logoutTime > currentTime ? NumberConversions.ceil((logoutTime - currentTime) / 1000D) : 0;
    }

    @Override
    public void run()
    {
        Player player = plugin.getPlayerCache().getPlayer(playerId);
        if (player == null)
        {
            cancel();
            return;
        }

        if (hasMoved(player))
        {
            DesireHCF.getLangHandler().sendRenderMessage(SessionHandler.getSession(player), "logout.cancelled");
            cancel();
            return;
        }

        EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("loguout.scoreboard"), String.valueOf((logoutTime - System.currentTimeMillis()) / 1000));

        // Safely logout the player once timer is up
        int remainingSeconds = getRemainingSeconds();
        if (remainingSeconds <= 0)
        {
            finished = true;
            TagHandler.clearTag(playerId);

            DesireHCF.getLangHandler().sendRenderMessage(SessionHandler.getSession(player), "logout.success");
            cancel();
            return;
        }

        // Inform player
        if (remainingSeconds < this.remainingSeconds)
        {

            DesireHCF.getLangHandler().sendRenderMessage(SessionHandler.getSession(player), "logout.pending",
                    "{remaining}", remainingSeconds + "");

            this.remainingSeconds = remainingSeconds;
        }
    }

    public void cancel()
    {
        super.cancel();
        Player p = PlayerUtils.getPlayer(playerId);
        if (p != null) {
            EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().getStringNoPrefix("loguout.scoreboard"));
        }
    }

    private boolean hasMoved(Player player)
    {
        Location l = player.getLocation();
        return loc.getWorld() != l.getWorld() || loc.getBlockX() != l.getBlockX() ||
                loc.getBlockY() != l.getBlockY() || loc.getBlockZ() != l.getBlockZ();
    }

    public static void run(DesireHCF plugin, Player player)
    {
        // Do nothing if player already has a task
        if (hasTask(player))
            return;

        // Calculate logout time
        long logoutTime = System.currentTimeMillis() + (TIMER * 1000);

        // Run the task every few ticks for accuracy
        SafeLogoutTask task = new SafeLogoutTask(plugin, player, logoutTime);
        task.runTaskTimer(plugin, 0, 5);

        // Cache the task
        tasks.put(player.getUniqueId(), task);
    }

    public static boolean hasTask(Player player)
    {
        SafeLogoutTask task = tasks.get(player.getUniqueId());
        if (task == null)
            return false;

        BukkitScheduler s = Bukkit.getScheduler();
        if (s.isQueued(task.getTaskId()) || s.isCurrentlyRunning(task.getTaskId()))
        {
            return true;
        }

        tasks.remove(player.getUniqueId());
        return false;
    }

    public static boolean isFinished(Player player)
    {
        return hasTask(player) && tasks.get(player.getUniqueId()).finished;
    }

    public static boolean cancel(Player player)
    {
        // Do nothing if player has no logout task
        if (!hasTask(player))
            return false;

        // Cancel logout task
        Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()).getTaskId());

        // Remove task early to prevent exploits
        tasks.remove(player.getUniqueId());

        return true;
    }

    public static void purgeFinished()
    {
        Iterator<SafeLogoutTask> iterator = tasks.values().iterator();
        BukkitScheduler s = Bukkit.getScheduler();

        while (iterator.hasNext())
        {
            int taskId = iterator.next().getTaskId();

            if (!s.isQueued(taskId) && !s.isCurrentlyRunning(taskId))
            {
                iterator.remove();
            }
        }
    }
}
