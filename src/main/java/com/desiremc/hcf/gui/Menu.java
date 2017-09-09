package com.desiremc.hcf.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Menu extends MenuHolder {

    private final String title;
    private int rows;
    public Inventory inventory;

    public Menu(String title, int rows) {
        super(9 * 6);

        this.title = title;
        this.rows = rows;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public int getMaxItems() {
        return this.rows * 9;
    }

    public boolean addMenuItem(MenuItem item, int x, int y) {
        return this.addMenuItem(item, (y * 9) + x);
    }

    public boolean addMenuItem(MenuItem item, int x, int y, short durability) {
        return this.addMenuItem(item, (y * 9) + x, durability);
    }

    public int getRows() {
        return this.rows;
    }

    public void destroy() {
        this.inventory = null;
        super.items = new MenuItem[this.items.length];
    }

    public void setRows(int newrows) {
        if (this.rows != newrows) {
            if (this.inventory != null) {
                this.inventory.clear();
            }

            this.rows = newrows;
            this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
            this.updateInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        }

        return this.inventory;
    }

    @Override
    protected MenuHolder clone() {
        MenuHolder clone = new Menu(this.title, this.rows);
        clone.setExitOnClickOutside(this.exitOnClickOutside);
        clone.setMenuCloseBehavior(this.menuCloseBehavior);
        clone.items = this.items.clone();
        clone.updateInventory();

        return clone;
    }
}
