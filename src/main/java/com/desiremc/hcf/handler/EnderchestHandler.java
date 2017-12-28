package com.desiremc.hcf.handler;

import com.desiremc.hcf.DesireHCF;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderchestHandler implements Listener
{

    public static boolean enderchestDisabled = DesireHCF.getConfigHandler().getBoolean("enderchest-disabled");

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
                    DesireHCF.getLangHandler().getString("enderchest.blocked");
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
        DesireHCF.getConfigHandler().setBoolean("enderchest-disabled", status);
    }
}