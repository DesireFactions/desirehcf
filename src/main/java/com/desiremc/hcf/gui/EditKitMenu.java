package com.desiremc.hcf.gui;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;

public class EditKitMenu extends Menu
{

    private EditState state;

    private int swapItem = -1;

    private HKit kit;

    public EditKitMenu(HKit kit)
    {
        super("§6§lEdit Kit: §e" + kit.getName(), 5);

        this.kit = kit;
    }

    @SuppressWarnings("deprecation")
    public void initialize()
    {
        removeAllItems();
        for (int i = 27; i <= 35; i++)
        {
            addMenuItem(MenuItem.empty("§8^^^ §7Contents §8^^^", Material.STAINED_GLASS_PANE, (short) 7, 1), i);
        }
        addMenuItem(new MenuItem("§cDelete Item", new MaterialData(Material.WOOL, (byte) 14), 1, (short) 14)
        {

            @Override
            public void onClick(Player player)
            {
                state = EditState.DELETEING;
                DesireHCF.getLangHandler().sendRenderMessage(player, "kits.delete_mode");
            }
        }, 39);
        addMenuItem(new MenuItem("§aSwap Items", new MaterialData(Material.WOOL, (byte) 5), 1, (short) 5)
        {

            @Override
            public void onClick(Player player)
            {
                state = EditState.SWAPPING;
                DesireHCF.getLangHandler().sendRenderMessage(player, "kits.swap_mode",
                        "{state}", "first");
            }
        }, 41);
        for (Entry<Integer, ItemStack> item : kit.getContentMap().entrySet())
        {
            addMenuItem(new MenuItem(item.getValue())
            {

                @Override
                public void onClick(Player player)
                {
                    if (state == EditState.DELETEING)
                    {
                        kit.removeItem(item.getKey());
                        state = null;
                        DesireHCF.getLangHandler().sendRenderMessage(player, "kits.delete_item",
                                "{item}", ChatColor.stripColor(ItemNames.lookup(getItemStack())));
                    }
                    else if (state == EditState.SWAPPING)
                    {
                        if (swapItem != -1)
                        {
                            if (swapItem == getSlot())
                            {
                                DesireHCF.getLangHandler().sendRenderMessage(player, "kits.different_swap");
                                return;
                            }
                            kit.swapItemOrder(getSlot(), swapItem);
                            swapItem = -1;
                            DesireHCF.getLangHandler().sendRenderMessage(player, "kits.swap_item",
                                    "{item1}", ChatColor.stripColor(ItemNames.lookup(items[swapItem].getItemStack())),
                                    "{item2}", ChatColor.stripColor(ItemNames.lookup(getItemStack())));
                            state = null;
                        }
                        else
                        {
                            swapItem = getSlot();
                            DesireHCF.getLangHandler().sendRenderMessage(player, "kits.swap_mode",
                                    "{state}", "second");
                        }
                    }
                    initialize();
                }
            }, item.getKey());
        }
        updateInventory();
    }

    private static enum EditState
    {
        DELETEING,
        SWAPPING;
    }

}
