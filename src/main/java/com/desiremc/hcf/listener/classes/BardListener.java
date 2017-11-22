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
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BardListener implements DesireClass
{

    private Cache<UUID, Long> cooldown;
    private int range;
    private int duration;

    public BardListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        duration = DesireHCF.getConfigHandler().getInteger("classes.bard.duration") * 20;

        cooldown = new Cache<>(duration / 20, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.bard.instant-cooldown-over");

                    HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());
                    if (PVPClass.BARD.equals(session.getPvpClass()))
                    {
                        ClassListener.applyPermanentEffects(PVPClass.BARD, p);
                    }
                }
            }
        }, DesireHCF.getInstance());

        range = DesireHCF.getConfigHandler().getInteger("classes.bard.range");
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

        if (!ClassListener.isClassItem(p.getItemInHand(), PVPClass.BARD))
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
            case EYE_OF_ENDER:
                showAllRogues(p);
                break;
            case BLAZE_POWDER:
                ClassListener.applyEffect(p, PotionEffectType.INCREASE_DAMAGE, "click", PVPClass.BARD, duration, range, true, true);
                break;
            case GHAST_TEAR:
                ClassListener.applyEffect(p, PotionEffectType.REGENERATION, "click", PVPClass.BARD, duration, range, true, true);
                break;
            case MAGMA_CREAM:
                ClassListener.applyEffect(p, PotionEffectType.FIRE_RESISTANCE, "click", PVPClass.BARD, duration, range, true, true);
                break;
            case SUGAR:
                ClassListener.applyEffect(p, PotionEffectType.SPEED, "click", PVPClass.BARD, duration, range, true, true);
                break;
            case IRON_INGOT:
                ClassListener.applyEffect(p, PotionEffectType.DAMAGE_RESISTANCE, "click", PVPClass.BARD, duration, range, true, true);
                break;
            case SPIDER_EYE:
                ClassListener.applyEffect(p, PotionEffectType.WITHER, "click", PVPClass.BARD, duration, range, false, true);
                break;
            case FEATHER:
                ClassListener.applyEffect(p, PotionEffectType.JUMP, "click", PVPClass.BARD, duration, range, true, true);
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

        if (!ClassListener.isClassItem(item, PVPClass.BARD))
        {
            return;
        }

        switch (item.getType())
        {
            case BLAZE_POWDER:
                ClassListener.applyEffect(p, PotionEffectType.INCREASE_DAMAGE, "hold", PVPClass.BARD, duration, range, true, false);
                break;
            case GHAST_TEAR:
                ClassListener.applyEffect(p, PotionEffectType.REGENERATION, "hold", PVPClass.BARD, duration, range, true, false);
                break;
            case MAGMA_CREAM:
                ClassListener.applyEffect(p, PotionEffectType.FIRE_RESISTANCE, "hold", PVPClass.BARD, duration, range, true, true);
                break;
            case SUGAR:
                ClassListener.applyEffect(p, PotionEffectType.SPEED, "hold", PVPClass.BARD, duration, range, true, false);
                break;
            case IRON_INGOT:
                ClassListener.applyEffect(p, PotionEffectType.DAMAGE_RESISTANCE, "hold", PVPClass.BARD, duration, range, true, false);
                break;
            case FEATHER:
                ClassListener.applyEffect(p, PotionEffectType.JUMP, "hold", PVPClass.BARD, duration, range, true, true);
                break;
        }
    }

    private void showAllRogues(Player source)
    {
        List<Player> players = FactionsUtils.getFactionMembersInRange(source, range);

        for (Player target : players)
        {
            HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());

            if (PVPClass.ROGUE.equals(session.getPvpClass()))
            {
                RogueListener.invisCooldown.put(target.getUniqueId(), System.currentTimeMillis());
            }
        }
    }
}