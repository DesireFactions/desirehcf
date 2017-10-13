package com.desiremc.hcf.handler;

import java.util.ArrayList;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.HCFCore;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.desiremc.core.DesireCore;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionLimiterHandler implements Listener
{

    private ArrayList<PotionLimit> potionLimits;

    public PotionLimiterHandler()
    {
        this.potionLimits = new ArrayList<>();
        this.loadPotionLimits();
    }

    private void loadPotionLimits()
    {
        ConfigurationSection configurationSection = DesireCore.getConfigHandler().getConfigurationSection("potion-limiter");
        for (String s : configurationSection.getKeys(false))
        {
            PotionLimit potionLimit = new PotionLimit();
            potionLimit.setType(PotionEffectType.getByName(s));
            potionLimit.setLevel(configurationSection.getInt(s + ".level"));
            potionLimit.setExtended(configurationSection.getBoolean(s + ".extended"));
            this.potionLimits.add(potionLimit);
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
                if (!isPotionAllowed((PotionMeta)potion.getItem().getItemMeta(),
                        potion.getShooter(), DesireCore.getConfigHandler().getBoolean("disabled-potion-msg")))
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onConsumeEvent(PlayerItemConsumeEvent event)
    {
        if (!event.getItem().getType().equals(Material.POTION)) return;

        Player p = event.getPlayer();
        PotionMeta potion = (PotionMeta) event.getItem().getItemMeta();

        for (PotionEffect effect : potion.getCustomEffects())
        {
            if (containsPotion(effect))
            {
                if (!isPotionAllowed(potion, p, DesireCore.getConfigHandler().getBoolean("disabled-potion-msg")))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPotionBrew(BrewEvent brewEvent)
    {
        BrewerInventory contents = brewEvent.getContents();
        ItemStack clone = contents.getIngredient().clone();
        ItemStack[] array = new ItemStack[3];
        for (int i = 0; i < 3; ++i)
        {
            if (contents.getItem(i) != null)
            {
                array[i] = contents.getItem(i).clone();
            }
        }
        new BukkitRunnable()
        {
            public void run()
            {
                for (ItemStack item : contents.getContents())
                {
                    PotionMeta meta = (PotionMeta)item.getItemMeta();
                    for (PotionEffect potionEffect : meta.getCustomEffects())
                    {
                        for (PotionLimit potionLimit : potionLimits)
                        {
                            if (potionLimit.getType().equals(potionEffect.getType()))
                            {
                                int level = potionLimit.getLevel();
                                int n = potionEffect.getAmplifier() + 1;
                                if (level == 0 || n > level)
                                {
                                    contents.setIngredient(clone);
                                    for (int j = 0; j < 3; ++j)
                                    {
                                        contents.setItem(j, array[j]);
                                    }
                                    return;
                                }
                                if (meta.getBasePotionData().isExtended() && !potionLimit.isExtended())
                                {
                                    contents.setIngredient(clone);
                                    for (int k = 0; k < 3; ++k)
                                    {
                                        contents.setItem(k, array[k]);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLater(HCFCore.getInstance(), 1L);
    }

    public boolean containsPotion(PotionEffect effect)
    {
        for (PotionLimit limits : potionLimits)
        {
            if (limits.getType().equals(effect.getType())) return true;
        }
        return false;
    }

    private PotionLimit getPotionLimit(PotionEffectType type)
    {
        for (PotionLimit limit : potionLimits)
        {
            if (limit.getType().equals(type)) return limit;
        }
        return null;
    }

    private boolean isPotionAllowed(PotionMeta potion, ProjectileSource source, boolean msg)
    {
        for(PotionEffect effect : potion.getCustomEffects())
        {
            PotionLimit limit = getPotionLimit(effect.getType());

            int potionLevel = effect.getAmplifier() + 1;
            int maxLevel = limit.getLevel();

            if (maxLevel == 0 || potionLevel > maxLevel || (potion.getBasePotionData().isExtended() && !limit.isExtended()))
            {
                if (msg)
                {
                    if (source instanceof Player)
                    {
                        Player p = (Player) source;
                        Session session = SessionHandler.getSession(p);
                        DesireCore.getLangHandler().sendRenderMessage(session, "potion-disabled");
                        p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    }
                }
                return false;
            }
        }
        return true;
    }

    public class PotionLimit
    {
        private PotionEffectType type;
        private int level;
        private boolean extended;

        public PotionEffectType getType()
        {
            return this.type;
        }

        public void setType(PotionEffectType type)
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
