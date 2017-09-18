
package com.desiremc.hcf.old_gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.desiremc.hcf.HCFCore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ItemMenu implements Listener {

    private ItemMenu parent;
    private String title;
    private HashMap<Integer, MenuItem> items;
    private Inventory inv;
    private int size;
    private final Player player;
    private int taskid = -1;
    private boolean closeOnClick;
    private boolean dead;
    private int cancel_task = -1;
    private Runnable closeAction;

    public ItemMenu(String title, Player player, int size) {
        this(null, title, player, size);
    }

    public ItemMenu(ItemMenu parent, String title, Player player, int size) {
        this.player = player;
        this.title = title;
        this.parent = parent;
        this.size = size;
        items = new HashMap<Integer, MenuItem>();
    }

    public void setCancelTask(int cancel_task) {
        this.cancel_task = cancel_task;
    }

    public void setCloseOnClick(boolean value) {
        closeOnClick = value;
    }

    public MenuItem addItem(int slot, MenuItem item) {
        items.put(slot, item);
        return item;
    }

    public MenuItem addItem(int x, int y, MenuItem item) {
        return addItem(x + y * 9, item);
    }

    public void clear() {
        items.clear();
        inv.clear();
    }

    public void show() {
        player.closeInventory();
        Bukkit.getPluginManager().registerEvents(this, HCFCore.getInstance());
        taskid = Bukkit.getScheduler().scheduleSyncDelayedTask(HCFCore.getInstance(), new Runnable() {
            public void run() {
                ItemMenu.this.close();
            }
        }, 20L * 300L);
        regenerate();
        player.openInventory(inv);
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public ItemMenu getParent() {
        return this.parent;
    }

    private int getSize() {
        return size;
    }

    public void regenerate() {
        Iterator<Map.Entry<Integer, MenuItem>> it = items.entrySet().iterator();
        inv = Bukkit.createInventory(player, getSize(), title);
        while (it.hasNext()) {
            Map.Entry<Integer, MenuItem> next = it.next();
            inv.setItem(next.getKey(), next.getValue().getItem().get());
        }
    }

    public void close() {
        HandlerList.unregisterAll(this);
        if (inv != null)
            player.closeInventory();
        if (taskid != -1)
            Bukkit.getScheduler().cancelTask(taskid);
        if (cancel_task != -1)
            Bukkit.getScheduler().cancelTask(cancel_task);
        dead = true;
        if (this.closeAction != null) {
            this.closeAction.run();
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void update() {
        inv.clear();
        for (Map.Entry<Integer, MenuItem> next : items.entrySet()) {
            inv.setItem(next.getKey(), next.getValue().getItem().get());
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void onClose(Runnable r) {
        this.closeAction = r;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer().equals(player))
            close();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (event.getWhoClicked().equals(player) && event.getCurrentItem() != null) {
            int slot = event.getSlot();
            MenuItem item = items.get(slot);
            if (item != null) {
                event.setCancelled(true);
                item.getExec().run();
                if (this.closeOnClick)
                    close();
            }
        }
    }

}
