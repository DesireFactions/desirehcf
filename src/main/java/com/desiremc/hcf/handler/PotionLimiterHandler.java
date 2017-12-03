package com.desiremc.hcf.handler;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
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
    public void onStartBrew(InventoryMoveItemEvent event)
    {
        if (event.getDestination().getType() != InventoryType.BREWING)
        {
            return;
        }
        
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event)
    {
        ThrownPotion potion = event.getPotion();

        for (PotionEffect effect : potion.getEffects())
        {
            if (containsPotion(effect))
            {
                if (!isPotionAllowed(Potion.fromItemStack(potion.getItem()), potion.getShooter()))
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onConsumeEvent(PlayerItemConsumeEvent event)
    {
        if (!event.getItem().getType().equals(Material.POTION))
            return;

        Player p = event.getPlayer();
        Potion potion = Potion.fromItemStack(event.getItem());

        for (PotionEffect effect : potion.getEffects())
        {
            if (containsPotion(effect))
            {
                if (!isPotionAllowed(potion, p, DesireHCF.getConfigHandler().getBoolean("send_potion_message")))
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPotionBrew(BrewEvent event)
    {
        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                ItemStack item;
                Potion potion;
                for (int i = 0; i < 3; i++)
                {
                    item = event.getContents().getContents()[i];
                    if (item != null && item.getType() == Material.POTION)
                    {
                        potion = Potion.fromItemStack(item);
                        if (!isPotionAllowed(potion))
                        {
                            item.setType(Material.AIR);
                        }
                    }
                }
            }
        }, 1L);
        BrewerInventory contents = event.getContents();
        ItemStack[] array = new ItemStack[3];
        for (int i = 0; i < 3; i++)
        {
            if (contents.getItem(i) != null)
            {
                array[i] = contents.getItem(i).clone();
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