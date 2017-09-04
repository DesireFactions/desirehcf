package me.borawski.hcf.gui;

import org.bukkit.entity.Player;

public abstract class MenuReadBehavior {
    private final boolean allowNullImput;

    public MenuReadBehavior() {
        this.allowNullImput = false;
    }

    public MenuReadBehavior(boolean allowNullImput) {
        this.allowNullImput = allowNullImput;
    }

    public abstract void onInputRead(Player player, String input_given);

    public final boolean allowNullInput() {
        return this.allowNullImput;
    }
}
