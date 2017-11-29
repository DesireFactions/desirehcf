package com.desiremc.hcf.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.ItemUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;

public class ViewKitsMenu extends Menu
{

    private HCFSession session;

    public ViewKitsMenu(HCFSession session)
    {
        super("§6§lKits", Math.max(1, (HKitHandler.getKitMap().size() + 8) / 9));
        this.session = session;
    }

    /**
     * Adds all the menu items to the display. If this is called multiple times, it has the capability of breaking.
     */
    public void initialize()
    {
        int i = 0;

        for (HKit kit : HKitHandler.getKits())
        {
            addMenuItem(KitMenuItem.getKitItem(session, kit), i);
            i++;
        }
    }

    private static class KitMenuItem extends MenuItem
    {

        private HKit kit;

        public KitMenuItem(ItemStack item, HCFSession session, HKit kit)
        {
            super(item);
            this.kit = kit;
        }

        @Override
        public void onClick(Player player)
        {
            HCFSession session = HCFSessionHandler.getHCFSession(player.getUniqueId());
            boolean onCooldown = session.hasKitCooldown(kit);
            boolean noPermission = session.getRank().getId() < kit.getId();
            if (noPermission)
            {
                DesireHCF.getLangHandler().sendRenderMessage(player, "kits.no_permission",
                        "{rank}", kit.getRequiredRank().getDisplayName());
            }
            else if (onCooldown)
            {
                DesireHCF.getLangHandler().sendRenderMessage(player, "kits.has_cooldown",
                        "{kit}", kit.getName(),
                        "{time}", DateUtils.formatDateDiff(session.getKitCooldown(kit) + System.currentTimeMillis()));
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(player, "kits.used_kit",
                        "{kit}", kit.getName());

                player.getInventory().addItem(ItemUtils.toArray(kit.getContents()));
                session.useKit(kit);
                getMenu().closeMenu(player);
            }
        }

        @SuppressWarnings("deprecation")
        public static KitMenuItem getKitItem(HCFSession session, HKit kit)
        {
            ItemStack item;
            boolean onCooldown = session.hasKitCooldown(kit);
            boolean noPermission = session.getRank().getId() < kit.getId();

            byte color;
            List<String> lore;

            if (onCooldown || noPermission)
            {
                color = (byte) 14;
            }
            else
            {
                color = (byte) 5;
            }

            if (noPermission)
            {
                lore = Arrays.asList("§4§lRequired Rank:",
                        "§c" + kit.getRequiredRank().getDisplayName());
            }
            else if (onCooldown)
            {
                lore = Arrays.asList("§4§lCooldown Ends:",
                        "§c" + DateUtils.formatDateDiff(session.getKitCooldown(kit) + System.currentTimeMillis()));
            }
            else
            {
                lore = Arrays.asList("§a§lAvailable");
            }

            item = new MaterialData(Material.WOOL, color).toItemStack();

            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);

            return new KitMenuItem(item, session, kit);
        }

    }

}
