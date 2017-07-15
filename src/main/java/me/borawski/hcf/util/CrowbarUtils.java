package me.borawski.hcf.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.borawski.hcf.Core;

public class CrowbarUtils {
    
    public static boolean isCrowbar(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() == Material.GOLD_HOE && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(Core.getInstance().getConfig().getString("crowbar-name")));
    }

    public static int getUses(ItemStack itemStack, String s) {
        if (itemStack != null && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals(Utils.chat(Core.getInstance().getConfig().getString("crowbar-name"))) && itemStack.getItemMeta().hasLore()) {
            int l = 0;
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.contains(s)) {
                    String level = lore.replace(Utils.chat(s), "");
                    l = Integer.parseInt(level);
                }
            }
            return l;
        }
        return 0;
    }

    public static ItemStack getNewCrowbar() {
        ItemStack itemStack = new ItemStack(Material.GOLD_HOE, 1);
        updateOrCreateCrowbarMeta(itemStack, Core.getInstance().getConfig().getInt("crowbar-uses-spawners"), Core.getInstance().getConfig().getInt("crowbar-uses-portals"));
        return itemStack;
    }

    public static ItemStack setItemName(final ItemStack itemStack, final String displayName) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.chat(displayName));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void updateOrCreateCrowbarMeta(ItemStack itemStack, int spawner, int portals) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("crowbar-name")));
        ArrayList<String> lore = new ArrayList<String>();
        Iterator<String> iterator = Core.getInstance().getConfig().getStringList("crowbar-lore").iterator();
        String spawnerCountLore = Core.getInstance().getConfig().getString("crowbar-spawners-string") + Core.getInstance().getConfig().getString("crowbar-spawners-uses-string") + Integer.toString(spawner);
        String portalsCountLore = Core.getInstance().getConfig().getString("crowbar-portals-string") + Core.getInstance().getConfig().getString("crowbar-portals-uses-string") + Integer.toString(portals);
        while (iterator.hasNext()) {
            lore.add(Utils.chat(iterator.next().replaceAll("<spawner-string>", spawnerCountLore).replaceAll("<portal-string>", portalsCountLore)));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

}
