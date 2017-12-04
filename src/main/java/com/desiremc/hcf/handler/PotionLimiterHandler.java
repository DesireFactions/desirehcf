package com.desiremc.hcf.handler;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.desiremc.core.DesireCore;
import com.desiremc.hcf.DesireHCF;

public class PotionLimiterHandler implements Listener
{

    private ArrayList<AllowedPotion> allowedPotions;

    public PotionLimiterHandler()
    {
        this.allowedPotions = new ArrayList<>();
        this.loadPotionLimits();
    }

    private void loadPotionLimits()
    {
        ConfigurationSection allowedSection = DesireHCF.getConfigHandler().getConfigurationSection("allowed_potions");
        for (String s : allowedSection.getKeys(false))
        {
            try
            {
                this.allowedPotions.add(new AllowedPotion(PotionType.getByEffect(PotionEffectType.getByName(s)), allowedSection.getInt(s + ".level"), allowedSection.getBoolean(s + ".extended")));
            }
            catch (Exception ex)
            {
                DesireHCF.getInstance().getLogger().severe("There was an error loading potion " + s + " for the potion limiter.");
            }
        }
    }

    @EventHandler
    public void onBrewComplete(BrewEvent event)
    {
        BrewerInventory inv = event.getContents();
        ItemStack ingredient = inv.getIngredient();
        Material ingredientType = ingredient.getType();
        ItemStack potItem;
        Potion pot;
        if (ingredientType == Material.GHAST_TEAR || ingredientType == Material.BLAZE_POWDER)
        {
            cancel(event, inv);
        }
        else if (ingredientType == Material.REDSTONE)
        {
            for (int i = 0; i < 3; i++)
            {
                potItem = inv.getItem(i);
                if (potItem != null && potItem.getType() == Material.POTION)
                {
                    pot = Potion.fromItemStack(potItem);
                    if (pot != null && pot.getType() == PotionType.INVISIBILITY)
                    {
                        cancel(event, inv);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() != Material.POTION)
        {
            return;
        }
        Potion pot = Potion.fromItemStack(event.getItem());
        if (pot != null)
        {
            if (!isPotionAllowed(pot))
            {
                cancel(event, null);
            }
        }
    }

    private static void cancel(Cancellable event, Inventory inv)
    {
        event.setCancelled(true);
        if (inv != null)
        {
            for (HumanEntity e : inv.getViewers())
            {
                if (e instanceof Player)
                {
                    DesireCore.getLangHandler().sendRenderMessage((Player) e, "potion.blocked");
                }
            }
        }
    }

    private AllowedPotion getAllowedPotion(PotionType type)
    {
        for (AllowedPotion allowed : allowedPotions)
        {
            if (allowed.getType().equals(type))
            {
                return allowed;
            }
        }
        return null;
    }

    private boolean isPotionAllowed(Potion potion)
    {
        AllowedPotion allowed = getAllowedPotion(potion.getType());
        return allowed == null || (potion.getLevel() <= allowed.getLevel() && (allowed.isExtended() || !potion.hasExtendedDuration()));
    }

    public class AllowedPotion
    {
        private PotionType type;
        private int level;
        private boolean extended;

        public AllowedPotion(PotionType type, int level, boolean extended)
        {
            this.type = type;
            this.level = level;
            this.extended = extended;
        }

        public PotionType getType()
        {
            return this.type;
        }

        public void setType(PotionType type)
        {
            this.type = type;
        }

        public int getLevel()
        {
            return this.level;
        }

        public void setLevel(int level)
        {
            this.level = level;
        }

        public boolean isExtended()
        {
            return this.extended;
        }

        public void setExtended(boolean extended)
        {
            this.extended = extended;
        }
    }
}