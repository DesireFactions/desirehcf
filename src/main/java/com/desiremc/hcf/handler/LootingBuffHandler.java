package com.desiremc.hcf.handler;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.desiremc.core.DesireCore;

public class LootingBuffHandler implements Listener
{

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e)
    {
        if (e.getEntity().getKiller() != null)
        {
            Player p = e.getEntity().getKiller();
            if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS))
            {
                int dropped = e.getDroppedExp();
                int bonus = DesireCore.getConfigHandler().getInteger("looting-buffer");
                e.setDroppedExp(dropped * bonus);
            }
        }
    }
}
