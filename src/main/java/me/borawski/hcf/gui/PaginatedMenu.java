package me.borawski.hcf.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PaginatedMenu extends MenuHolder {

    private MenuItem[][] true_items;
    private Inventory[] inventories;
    private String title;
    private int rows;

    private PaginatedMenu(String title, int rows, int initial_pages) {
        super(9 * rows);

        if (initial_pages < 1) {
            throw new IllegalArgumentException("Cannot instantiate a PaginatedMenu with a nonpositive page count!");
        }

        this.true_items = new MenuItem[initial_pages][this.getMaxItems()];
        this.title = title;
        this.rows = rows;
        this.inventories = new Inventory[initial_pages];

        for (int i = 0; i < initial_pages; i++) {
            this.inventories[i] = Bukkit.createInventory(this, 9 * rows, title);
        }
    }

    @SuppressWarnings("deprecation")
    protected void selectMenuItem(Inventory inventory, Player player, int index) {
        if ((index > -1) && (index < this.getMaxItems())) {
            for (int i = 0; i < this.inventories.length; i++) {
                Inventory inv = this.inventories[i];

                if (inv == null) {
                    continue;
                }

                if (inv.hashCode() == inventory.hashCode()) {
                    // We have a match, and ideally no duplicate Inventories
                    // will be in the array
                    MenuItem item = this.true_items[i][index];

                    if (item != null) {
                        item.onClick(player);
                    }

                    break;
                }
            }
        }

        player.updateInventory();
    }

    /**
     * Open the Inventory for the given Player, defaulting to the first
     * (zero-index) page.
     *
     * @param player
     *            The Player to open for.
     */
    @Override
    public void openMenu(Player player) {
        this.openMenu(0, player);
    }

    /**
     * Open the Inventory for the given Player, on the given page.
     *
     * @param page
     *            The page to open.
     * @param player
     *            The Player to open for.
     */
    void openMenu(int page, Player player) {
        boolean open = this.isViewingPage(page, player);

        if (!open) {
            player.openInventory(this.getInventory(page));
        } else {
            throw new IllegalStateException(player.getName() + " is already viewing " + this.getInventory(page).getTitle());
        }
    }

    /**
     * Closes the Player's open inventory, provided that they are viewing a page
     * of this Menu.
     *
     * @param player
     *            The Player to close for.
     */
    @Override
    public void closeMenu(Player player) {
        boolean open = this.isViewingAny(player);

        if (open) {
            player.closeInventory();
        }
    }

    /**
     * Update this PaginatedMenu, for all viewers of all pages.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void updateMenu() {
        for (Inventory inv : this.inventories) {
            if (inv == null) {
                continue;
            }

            for (HumanEntity player : inv.getViewers()) {
                if (player instanceof Player) {
                    ((Player) player).updateInventory();
                }
            }
        }
    }

    /**
     * Update this PaginatedMenu for all viewers of the given page.
     *
     * @param page
     *            The page to update viewers of.
     */
    @SuppressWarnings("deprecation")
    public void updateMenu(int page) {
        for (HumanEntity player : this.getInventory(page).getViewers()) {
            if (player instanceof Player) {
                ((Player) player).updateInventory();
            }
        }
    }

    /**
     * Get whether or not the given player is currently viewing the given page
     * on this Menu.
     *
     * @param page
     *            The page to check.
     * @param player
     *            The Player to check.
     * @return <b>true</b> if the given Player is viewing the given page,
     *         <b>false</b> otherwise.
     */
    boolean isViewingPage(int page, Player player) {
        return this.getInventory(page).getViewers().contains(player);
    }

    /**
     * Get whether or not the given player is currently viewing any page on this
     * Menu.
     *
     * @param player
     *            The Player to check.
     * @return <b>true</b> if the given Player is viewing any page of this Menu,
     *         <b>false</b> otherwise.
     */
    boolean isViewingAny(Player player) {
        for (Inventory inv : this.inventories) {
            if (inv == null) {
                continue;
            }

            if (inv.getViewers().contains(player)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the Inventory, defaulting to the first (zero-index) page.
     */
    @Override
    public Inventory getInventory() {
        return this.getInventory(0);
    }

    /**
     * Get the Inventory for the given page.
     *
     * @param page
     *            The page that should be returned.
     * @return An Inventory for the given page.
     */
    Inventory getInventory(int page) {
        if ((page < 0) || (page >= this.getPageCount())) {
            return null;
        }

        Inventory inv = this.inventories[page];

        if (inv == null) {
            this.inventories[page] = Bukkit.createInventory(this, 9 * this.rows, this.title);
            inv = this.inventories[page];
        }

        return inv;
    }

    /**
     * Get the total number of pages currently in this PaginatedMenu.
     *
     * @return The total number of pages.
     */
    int getPageCount() {
        return this.true_items.length;
    }

    /**
     * Get the total number of items per page in this menu; this is simply nine
     * times the amount of rows that were given in the constructor.
     *
     * @return The number of items per page.
     */
    public int getItemsPerPage() {
        return this.getMaxItems();
    }

    /**
     * Set the given MenuItem[] to the given page, specifying whether to
     * completely overwrite the page or not. The operation will fail if the
     * given MenuItem[] is not of the appropriate length; use
     * {@link PaginatedMenu#getItemsPerPage()} to get this length.
     *
     * @param page
     *            The page to change.
     * @param set_to
     *            The MenuItem[] to use to make the change.
     * @param overwrite
     *            If false, existing items on the page will not be overwritten,
     *            only currently null items will be replaced with those from the
     *            given MenuItem[].
     * @return <b>true</b> if the operation was successful, <b>false</b>
     *         otherwise.
     */
    public boolean setPage(int page, MenuItem[] set_to, boolean overwrite) {
        if (set_to.length != this.getMaxItems()) {
            return false;
        }

        if (overwrite) {
            this.true_items[page] = set_to.clone();
            for (int i = 0; i < this.getMaxItems(); i++) {
                this.removeMenuItem(page, i);

                if (set_to[i] == null) {
                    continue;
                }

                this.addMenuItem(page, set_to[i], i);
            }
        } else {
            for (int i = 0; i < this.getMaxItems(); i++) {
                if (set_to[i] != null) {
                    this.addMenuItem(page, set_to[i], i);
                }
            }
        }

        return true;
    }

    /**
     * Add a new page and subsequently assign it items based on the given
     * MenuItem[].
     *
     * @param new_page
     *            A MenuItem[] of an appropriate length for this PaginatedMenu;
     *            use {@link PaginatedMenu#getItemsPerPage()} to find the
     *            appropriate length.
     * @return <b>true</b> if the operation was successful, <b>false</b>
     *         otherwise.
     */
    public boolean addPage(MenuItem[] new_page) {
        if (this.getMaxItems() != new_page.length) {
            return false;
        }

        this.addPages(1);
        int page = this.getPageCount() - 1;

        for (int i = 0; i < this.getMaxItems(); i++) {
            if (new_page[i] == null) {
                continue;
            }

            this.addMenuItem(page, new_page[i], i);
        }

        return true;
    }

    /**
     * Add one page to this PaginatedMenu; the page will be initialized as
     * empty.
     */
    public void addPage() {
        this.addPages(1);
    }

    /**
     * Add the given number of pages to this PaginatedMenu; the pages will be
     * initialized as empty.
     *
     * @param count
     *            The number of pages to add.
     */
    void addPages(int count) {
        MenuItem[][] new_items = new MenuItem[this.true_items.length + count][this.getMaxItems()];

        System.arraycopy(this.true_items, 0, new_items, 0, this.getPageCount());

        Inventory[] new_inventories = new Inventory[this.true_items.length + count];

        System.arraycopy(this.inventories, 0, new_inventories, 0, this.inventories.length);

        for (int i = this.inventories.length; i < new_inventories.length; i++) {
            new_inventories[i] = Bukkit.createInventory(this, 9 * this.rows, this.title);
        }

        this.true_items = new_items;
        this.inventories = new_inventories;
    }

    /**
     * Add a MenuItem to the given index; defaults to the first (zero-index)
     * page.
     */
    @Override
    public boolean addMenuItem(MenuItem item, int index) {
        return this.addMenuItem(0, item, index);
    }

    /**
     * Add a MenuItem to the given index, on the given page.
     */
    boolean addMenuItem(int page, MenuItem item, int index) {
        if ((page < 0) || (page >= this.getPageCount())) {
            return false;
        }

        ItemStack slot = this.getInventory(page).getItem(index);

        if ((slot != null) && !slot.getType().equals(Material.AIR)) {
            return false;
        } else if ((index < 0) || (index >= this.getMaxItems())) {
            return false;
        }

        this.getInventory(page).setItem(index, item.getItemStack());
        this.true_items[page][index] = item;
        item.addToMenu(this);

        return true;
    }

    /**
     * Remove the MenuItem at the given index; defaults to the first
     * (zero-index) page.
     */
    @Override
    public boolean removeMenuItem(int index) {
        return this.removeMenuItem(0, index);
    }

    /**
     * Remove the MenuItem at the given index, on the given page.
     */
    boolean removeMenuItem(int page, int index) {
        if ((page < 0) || (page >= this.getPageCount())) {
            return false;
        }

        ItemStack slot = this.getInventory(page).getItem(index);

        if ((slot == null) || slot.getType().equals(Material.AIR)) {
            return false;
        } else if ((index < 0) || (index >= this.getMaxItems())) {
            return false;
        }

        this.getInventory(page).clear(index);
        MenuItem remove = this.true_items[page][index];
        this.true_items[page][index] = null;
        remove.removeFromMenu(this);

        return true;
    }

    /**
     * Get a Menu that represents the given page of this PaginatedMenu.
     *
     * @param page
     *            The page to convert to a Menu.
     * @return The page as a standalone Menu.
     */
    public Menu toMenu(int page) {
        Menu menu = new Menu(this.title, this.rows);
        menu.setExitOnClickOutside(this.exitOnClickOutside);
        menu.setMenuCloseBehavior(this.menuCloseBehavior);
        menu.items = this.true_items[page].clone();

        return menu;
    }

    @Override
    protected PaginatedMenu clone() {
        PaginatedMenu clone = new PaginatedMenu(this.title, this.rows, this.getPageCount());
        clone.setExitOnClickOutside(this.exitOnClickOutside);
        clone.setMenuCloseBehavior(this.menuCloseBehavior);
        clone.true_items = this.true_items.clone();
        clone.inventories = this.inventories.clone();

        return clone;
    }
}
