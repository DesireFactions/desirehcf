package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BardListener implements DesireClass
{

    private Cache<UUID, Long> cooldown;
    private Cache<UUID, Long> timedEffects;

    @Override
    public void initialize()
    {
        cooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.bard.instant-cooldown"), TimeUnit
                .SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.bard.instant-cooldown-over");
                }
            }
        }, DesireHCF.getInstance());

        timedEffects = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.bard.frequency"), TimeUnit
                .SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    applyEffect(p);
                    timedEffects.put(p.getUniqueId(), System.currentTimeMillis());
                }
            }
        }, DesireHCF.getInstance());
    }

    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!session.getPvpClass().equals(PVPClass.BARD))
        {
            return;
        }

        if (cooldown.get(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendString(p, "classes.bard.on-cooldown");
            return;
        }

        switch (p.getItemInHand().getType())
        {
            case SPECKLED_MELON:
                healInRange(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger
                        ("classes.bard.distance")));
                break;
            case WHEAT:
                feedInRange(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger
                        ("classes.bard.distance")));
                break;
            case EYE_OF_ENDER:
                showAllRogues(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger
                        ("classes.bard.rouge-finder.range")));
                break;
        }

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void playerItemHeldEvent(PlayerItemHeldEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!session.getPvpClass().equals(PVPClass.BARD))
        {
            return;
        }

        if (timedEffects.get(p.getUniqueId()) != null || cooldown.get(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendString(p, "classes.bard.on-cooldown");
            return;
        }

        applyEffect(p);
    }

    private void applyEffect(Player p)
    {
        switch (p.getItemInHand().getType())
        {
            case BLAZE_ROD:
                applyStrength(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger
                        ("classes.bard.distance")));
                timedEffects.put(p.getUniqueId(), System.currentTimeMillis());
                break;
            case GHAST_TEAR:
                applyRegen(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes" +
                        ".bard.distance")));
                timedEffects.put(p.getUniqueId(), System.currentTimeMillis());
                break;
            case MAGMA_CREAM:
                applyFire(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes" +
                        ".bard.distance")));
                timedEffects.put(p.getUniqueId(), System.currentTimeMillis());
                break;
            case SUGAR:
                applySpeed(FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes" +
                        ".bard.distance")));
                timedEffects.put(p.getUniqueId(), System.currentTimeMillis());
                break;
        }
    }

    private void healInRange(List<Player> players)
    {
        double healthAmount = DesireHCF.getConfigHandler().getDouble("classes.bard.instant-effect-strength.heal");
        for (Player target : players)
        {
            double health = target.getHealth() + healthAmount;

            if (health > 20)
            {
                target.setHealth(20);
            }
            else
            {
                target.setHealth(health);
            }
        }
    }

    private void feedInRange(List<Player> players)
    {
        int feedAmount = DesireHCF.getConfigHandler().getInteger("classes.bard.instant-effect-strength.feed");
        for (Player target : players)
        {
            int foodLevel = target.getFoodLevel() + feedAmount;

            if (foodLevel > 20)
            {
                target.setFoodLevel(20);
            }
            else
            {
                target.setFoodLevel(foodLevel);
            }
        }
    }

    private void showAllRogues(List<Player> players)
    {
        for (Player target : players)
        {
            HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());

            if (session.getPvpClass().equals(PVPClass.ROGUE))
            {
                RogueListener.caughtByBard(target);
            }
        }
    }

    private void applyStrength(List<Player> players)
    {
        for (Player target : players)
        {
            PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.duration"), DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.timed-effect-strength.strength"));
            target.addPotionEffect(strength);
        }
    }

    private void applyRegen(List<Player> players)
    {
        for (Player target : players)
        {
            PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.duration"), DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.timed-effect-strength.regen"));
            target.addPotionEffect(regen);
        }
    }

    private void applyFire(List<Player> players)
    {
        for (Player target : players)
        {
            PotionEffect fire = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.duration"), DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.timed-effect-strength.fire"));
            target.addPotionEffect(fire);
        }
    }

    private void applySpeed(List<Player> players)
    {
        for (Player target : players)
        {
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.duration"), DesireHCF.getConfigHandler()
                    .getInteger("classes.bard.timed-effect-strength.speed"));
            target.addPotionEffect(speed);
        }
    }
}