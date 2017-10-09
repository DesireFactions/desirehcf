package com.desiremc.hcf.handler;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public class EnderchestHandler implements Listener
{

    public static boolean enderchestDisabled = DesireCore.getConfigHandler().getBoolean("enderchest-disabled");

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getType() == Material.ENDER_CHEST)
            {
                if (enderchestDisabled)
                {
                    event.setCancelled(true);
                    DesireCore.getLangHandler().getString("enderchest.blocked");
                }
                else
                {
                    Session s = SessionHandler.getSession(event.getPlayer());
                    if (!s.hasAchievement(Achievement.FIRST_ENDERCHEST_OPEN))
                    {
                        s.awardAchievement(Achievement.FIRST_ENDERCHEST_OPEN, true);
                    }
                }
            }
        }
    }

    public static boolean getEnderChestStatus()
    {
        return enderchestDisabled;
    }

    public static void setEnderchestStatus(boolean status)
    {
        enderchestDisabled = status;
        DesireCore.getConfigHandler().setBoolean("enderchest-disabled", status);
    }
}