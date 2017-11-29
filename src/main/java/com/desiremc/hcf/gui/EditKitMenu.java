package com.desiremc.hcf.gui;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
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

        initialize();
    }

    @SuppressWarnings("deprecation")
    private void initialize()
    {
        removeAllItems();
        for (int i = 27; i <= 35; i++)
        {
            addMenuItem(MenuItem.empty("§7^^^Contents§7^^^", Material.STAINED_GLASS_PANE, (short) 8, 1), i);
        }
        addMenuItem(new MenuItem("§cDelete Item", new MaterialData(Material.WOOL, (byte) 14), 1, (short) 14)
        {

            @Override
            public void onClick(Player player)
            {
                state = EditState.DELETEING;
            }
        }, 37);
        addMenuItem(new MenuItem("§aSwap Items", new MaterialData(Material.WOOL, (byte) 5), 1, (short) 5)
        {

            @Override
            public void onClick(Player player)
            {
                state = EditState.SWAPPING;
            }
        }, 37);
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
                        EditKitMenu.this.removeMenuItem(item.getKey());
                    }
                    else if (state == EditState.SWAPPING)
                    {
                        if (swapItem != -1)
                        {
                            Map<Integer, ItemStack> items = kit.getContentMap();
                            ItemStack item = items.get(swapItem);
                            items.put(getSlot(), item);
                            items.put(swapItem, getItemStack());
                        }
                        else
                        {
                            swapItem = getSlot();
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
