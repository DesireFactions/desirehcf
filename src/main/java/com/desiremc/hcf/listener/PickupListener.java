package com.desiremc.hcf.listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupListener implements Listener
{

    private static Set<UUID> disabledCobble = new HashSet<>();

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e)
    {
        if (disabledCobble.contains(e.getPlayer().getUniqueId()))
        {
            e.setCancelled(true);
        }
    }

    /**
     * Toggles cobblestone mode on and off.
     * 
     * @param uuid the player to toggle.
     * @return whether or not the player previously had it toggled.
     */
    public static boolean toggleCobble(UUID uuid)
    {
        if (disabledCobble.contains(uuid))
        {
            disabledCobble.remove(uuid);
            return true;
        }
        disabledCobble.add(uuid);
        return false;
    }

}
