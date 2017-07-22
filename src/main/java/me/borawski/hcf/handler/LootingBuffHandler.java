package me.borawski.hcf.handler;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.borawski.hcf.Core;

public class LootingBuffHandler implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        if (e.getEntity().getKiller() instanceof Player) {
            Player p = e.getEntity().getKiller();
            if (p.getItemInHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                int dropped = e.getDroppedExp();
                int bonus = Core.getInstance().getConfig().getInt("looting-buffer");
                e.setDroppedExp(dropped * bonus);
            }
        }
    }
}
