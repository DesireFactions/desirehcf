package com.desiremc.hcf.util;

import org.bukkit.inventory.ItemStack;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @Since Jul 30, 2015 6:46:16 PM
 */
public enum ArmorType
{
    HELMET(5),
    CHESTPLATE(6),
    LEGGINGS(7),
    BOOTS(8);

    private final int slot;

    ArmorType(int slot)
    {
        this.slot = slot;
    }

    /**
     * Attempts to match the ArmorType for the specified ItemStack.
     *
     * @param itemStack The ItemStack to parse the type of.
     * @return The parsed ArmorType. (null if none were found.)
     */
    public final static ArmorType matchType(final ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return null;
        }
        switch (itemStack.getType())
        {
            case DIAMOND_HELMET:
                return HELMET;
            case GOLD_HELMET:
                return HELMET;
            case IRON_HELMET:
                return HELMET;
            case CHAINMAIL_HELMET:
                return HELMET;
            case LEATHER_HELMET:
                return HELMET;
            case PUMPKIN:
                return HELMET;
            case DIAMOND_CHESTPLATE:
                return CHESTPLATE;
            case GOLD_CHESTPLATE:
                return CHESTPLATE;
            case IRON_CHESTPLATE:
                return CHESTPLATE;
            case CHAINMAIL_CHESTPLATE:
                return CHESTPLATE;
            case LEATHER_CHESTPLATE:
                return CHESTPLATE;
            case DIAMOND_LEGGINGS:
                return LEGGINGS;
            case GOLD_LEGGINGS:
                return LEGGINGS;
            case IRON_LEGGINGS:
                return LEGGINGS;
            case CHAINMAIL_LEGGINGS:
                return LEGGINGS;
            case LEATHER_LEGGINGS:
                return LEGGINGS;
            case DIAMOND_BOOTS:
                return BOOTS;
            case GOLD_BOOTS:
                return BOOTS;
            case IRON_BOOTS:
                return BOOTS;
            case CHAINMAIL_BOOTS:
                return BOOTS;
            case LEATHER_BOOTS:
                return BOOTS;
            default:
                return null;
        }
    }

    public int getSlot()
    {
        return slot;
    }
}