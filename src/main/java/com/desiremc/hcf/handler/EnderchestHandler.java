package com.desiremc.hcf.handler;

import com.desiremc.hcf.HCFCore;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.MscAchievements;

public class EnderchestHandler implements Listener
{

    public static boolean enderchestDisabled = HCFCore.getConfigHandler().getBoolean("enderchest-disabled");

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
                    event.getPlayer().sendMessage("EnderChest are disabled!");
                } else
                {
                    Session s = SessionHandler.getSession(event.getPlayer());
                    if (!s.hasAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN.getId()))
                    {
                        s.awardAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN, true);
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
        HCFCore.getConfigHandler().setBoolean("enderchest-disabled", status);
    }
}