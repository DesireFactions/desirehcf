package com.desiremc.hcf.gui;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class MenuHolder extends MenuBase implements InventoryHolder {

    MenuHolder(int max_items) {
        super(max_items);
    }

    @Override
    public void openMenu(Player player) {
        if (this.getInventory().getViewers().contains(player)) {
            throw new IllegalStateException(player.getName() + " is already viewing " + this.getInventory().getTitle());
        }

        player.openInventory(this.getInventory());
    }

    @Override
    public void closeMenu(Player player) {
        if (this.getInventory().getViewers().contains(player)) {
            this.getInventory().getViewers().remove(player);
            player.closeInventory();
        }
    }

    @SuppressWarnings("deprecation")
    public boolean selectMenuItem(Inventory inventory, Player player, int index, InventoryClickEvent e) {
        boolean allow = false;

        if ((index > -1) && (index < this.getMaxItems())) {
            MenuItem item = this.items[index];

            if (item != null) {
                allow = item.onClick(player, e);
            }
        }

        player.updateInventory();
        return allow;
    }

    public boolean addMenuItem(MenuItem item, int index) {
        ItemStack slot = this.getInventory().getItem(index);

        if ((slot != null) && (slot.getType() != Material.AIR)) {
            return false;
        } else if ((index < 0) || (index >= this.getMaxItems())) {
            return false;
        }

        this.getInventory().setItem(index, item.getItemStack());
        this.items[index] = item;
        item.addToMenu(this);

        return true;
    }

    public boolean removeMenuItem(int index) {
        ItemStack slot = this.getInventory().getItem(index);

        if ((slot == null) || (slot.getType() == Material.AIR)) {
            return false;
        } else if ((index < 0) || (index >= this.getMaxItems())) {
            return false;
        }

        this.getInventory().clear(index);
        MenuItem remove = this.items[index];
        this.items[index] = null;

        if (remove != null) {
            remove.removeFromMenu(this);
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public void updateMenu() {
        for (HumanEntity entity : this.getInventory().getViewers()) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                player.updateInventory();
            }
        }
    }

    public void updateInventory() {
        this.getInventory().clear();

        for (int i = 0; i < this.getMaxItems(); i++) {
            MenuItem item = super.items[i];

            if (item != null) {
                this.getInventory().setItem(i, item.getItemStack());
            }
        }
    }

    @Override
    public abstract Inventory getInventory();

    @Override
    protected abstract MenuHolder clone();
}