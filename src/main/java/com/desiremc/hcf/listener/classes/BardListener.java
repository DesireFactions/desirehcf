package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BardListener implements DesireClass
{

    private Cache<UUID, Long> cooldown;
    private List<Material> classItems;

    public BardListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        cooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.bard.instant-cooldown"), TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.bard.instant-cooldown-over");
                }
            }
        }, DesireHCF.getInstance());

        classItems = Arrays.asList(Material.EYE_OF_ENDER, Material.BLAZE_POWDER, Material.GHAST_TEAR, Material.MAGMA_CREAM, Material.SUGAR,
                Material.IRON_AXE, Material.SPIDER_EYE, Material.FEATHER);
    }

    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.BARD.equals(session.getPvpClass()))
        {
            return;
        }

        if (cooldown.get(p.getUniqueId()) != null)
        {
            if (isClassItem(p.getItemInHand()))
            {
                DesireHCF.getLangHandler().sendString(p, "classes.bard.on-cooldown");
            }
            return;
        }

        List<Player> players = FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.range"));

        switch (p.getItemInHand().getType())
        {
            case EYE_OF_ENDER:
                showAllRogues(p);
                break;
            case BLAZE_POWDER:
                PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.STRENGTH.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(strength);
                }
                break;
            case GHAST_TEAR:
                PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.REGEN.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(regen);
                }
                break;
            case MAGMA_CREAM:
                PotionEffect fireRes = new PotionEffect(PotionEffectType.FIRE_RESISTANCE,
                        180 * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.FIRE_RESISTANCE.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(fireRes);
                }
                break;
            case SUGAR:
                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.SPEED.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(speed);
                }
                break;
            case IRON_INGOT:
                PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.RESISTANCE.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(resistance);
                }
                break;
            case SPIDER_EYE:
                PotionEffect wither = new PotionEffect(PotionEffectType.WITHER,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.WITHER.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(wither);
                }
                break;
            case FEATHER:
                PotionEffect jumpBoost = new PotionEffect(PotionEffectType.JUMP,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.bard.effects.JUMP_BOOST.click") - 1);
                for (Player target : players)
                {
                    target.addPotionEffect(jumpBoost);
                }
                break;
            default:
                return;
        }

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void playerItemHeldEvent(PlayerItemHeldEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.BARD.equals(session.getPvpClass()))
        {
            return;
        }

        ItemStack item = p.getInventory().getItem(event.getNewSlot());

        if (item == null || item.getType().equals(Material.AIR))
        {
            return;
        }

        if (!isClassItem(item))
        {
            return;
        }

        applyEffect(p, item);
    }

    private void applyEffect(Player p, ItemStack item)
    {
        List<Player> players = FactionsUtils.getFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.bard.range"));

        switch (item.getType())
        {
            case BLAZE_POWDER:
                for (Player target : players)
                {
                    PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.timed-effect-strength.strength") - 1);
                    target.addPotionEffect(strength);
                }
                break;
            case GHAST_TEAR:
                for (Player target : players)
                {
                    PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.timed-effect-strength.regen"));
                    target.addPotionEffect(regen);
                }
                break;
            case MAGMA_CREAM:
                for (Player target : players)
                {
                    PotionEffect fire = new PotionEffect(PotionEffectType.FIRE_RESISTANCE,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.timed-effect-strength.fire"));
                    target.addPotionEffect(fire);
                }
                break;
            case SUGAR:
                for (Player target : players)
                {
                    PotionEffect speed = new PotionEffect(PotionEffectType.SPEED,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.effects.SPEED.hold"));
                    target.addPotionEffect(speed);
                }
                break;
            case IRON_INGOT:
                for (Player target : players)
                {
                    PotionEffect speed = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.effects.RESISTANCE.hold"));
                    target.addPotionEffect(speed);
                }
                break;
            case FEATHER:
                for (Player target : players)
                {
                    PotionEffect effect = new PotionEffect(PotionEffectType.JUMP,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20,
                            DesireHCF.getConfigHandler().getInteger("classes.bard.effects.JUMP_BOOST.hold"));
                    target.addPotionEffect(effect);
                }
                break;
        }
    }

    private void showAllRogues(Player source)
    {
        List<Player> players = FactionsUtils.getFactionMembersInRange(source, DesireHCF.getConfigHandler().getInteger("classes.bard.range"));

        for (Player target : players)
        {
            HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());

            if (session.getPvpClass().equals(PVPClass.ROGUE))
            {
                RogueListener.caughtByBard(target);
            }
        }
    }

    private boolean isClassItem(ItemStack item)
    {
        return classItems.contains(item.getType());
    }
}