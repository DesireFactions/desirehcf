package me.borawski.hcf.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public abstract class MenuItem extends MenuClickBehavior {

    private MenuBase menu;
    private int quantity;
    private MaterialData icon;
    private String text;
    private List<String> descriptions = new ArrayList<>();
    private ItemStack item;

    // Additional Values
    private short data = 0;
    private int slot = 0;

    public MenuItem(String text) {
        this(text, new MaterialData(Material.PAPER));
    }

    public MenuItem(String name, ItemStack is) {
        item = is;
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                item.setItemMeta(meta);
            }
        }
    }

    public MenuItem(String text, MaterialData icon) {
        this(text, icon, 1);
    }

    @SuppressWarnings("deprecation")
    private MenuItem(String text, MaterialData icon, int quantity) {
        this.text = text;
        this.icon = icon;
        this.quantity = quantity;
        this.data = icon.getData();
    }

    public MenuItem(String text, MaterialData icon, short data) {
        this.text = text;
        this.icon = icon;
        this.quantity = 1;
        this.data = data;
    }

    public MenuItem(String text, MaterialData icon, int quantity, short data) {
        this.text = text;
        this.icon = icon;
        this.quantity = quantity;
        this.data = data;
    }

    public void addToMenu(MenuBase menu) {
        this.menu = menu;
    }

    public void removeFromMenu(MenuBase menu) {
        if (this.menu == null) {
            this.menu = null;
        }
    }

    public MenuBase getMenu() {
        return this.menu;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MaterialData getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public void setDescriptions(List<String> lines) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setLore(lines);
                item.setItemMeta(meta);
            }
        }
        this.descriptions = lines;
    }

    public ItemStack getItemStack() {
        if (item != null) {
            net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tagCompound = itemStack.getTag();
            if (tagCompound == null) {
                tagCompound = new NBTTagCompound();
            }
            tagCompound.setInt("Unbreakable", 1);
            tagCompound.setInt("HideFlags", 6);
            itemStack.setTag(tagCompound);
            return CraftItemStack.asBukkitCopy(itemStack);
        }

        ItemStack slot = new ItemStack(this.getIcon().getItemType(), this.getQuantity(), this.data);
        ItemMeta meta = slot.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(this.getText());
            meta.setLore(this.descriptions);
            slot.setItemMeta(meta);
        }

        net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(slot);
        NBTTagCompound tagCompound = itemStack.getTag();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        tagCompound.setInt("Unbreakable", 1);
        tagCompound.setInt("HideFlags", 6);
        itemStack.setTag(tagCompound);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public void setData(short data) {
        this.data = data;
    }

    public short getData() {
        return this.data;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    @Override
    public abstract void onClick(Player player);

    public boolean onClick(Player player, InventoryClickEvent e) {
        this.onClick(player);
        return false;
    }
}
