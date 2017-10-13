package com.desiremc.hcf.handler;

import com.desiremc.hcf.HCFCore;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

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
                int bonus = HCFCore.getConfigHandler().getInteger("looting-buffer");
                e.setDroppedExp(dropped * bonus);
            }
        }
    }
}
