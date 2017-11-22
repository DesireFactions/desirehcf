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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RogueListener implements DesireClass
{

    public static Cache<UUID, Long> invisCooldown;
    private Cache<UUID, Long> cooldown;

    public RogueListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        invisCooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.rogue.uninvis-timer"), TimeUnit
                .SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.rogue.uninvis-over");
                }
            }
        }, DesireHCF.getInstance());
        cooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.rogue.cooldown"), TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.rogue.effect-over");

                    HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());
                    if (PVPClass.ROGUE.equals(session.getPvpClass()))
                    {
                        ClassListener.applyPermanentEffects(PVPClass.ROGUE, p);
                    }
                }
            }
        }, DesireHCF.getInstance());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.ROGUE.equals(session.getPvpClass()))
            return;

        if (!PlayerUtils.hasEffect(p, PotionEffectType.INVISIBILITY))
            return;

        if (FactionsUtils.getNonFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.rogue" +
                ".uninvis-range")).size() > 0)
        {
            invisCooldown.put(p.getUniqueId(), System.currentTimeMillis());
            DesireHCF.getLangHandler().sendString(p, "classes.rogue.shown");
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof Player))
            return;

        Player target = (Player) event.getEntity();
        Player player = (Player) event.getDamager();

        double degrees = player.getLocation().getDirection().angle(target.getLocation().getDirection()) / 180 * Math.PI;

        if (degrees < 225 || degrees > 315)
            return;

        HCFSession targetSession = HCFSessionHandler.getHCFSession(target.getUniqueId());
        HCFSession playerSession = HCFSessionHandler.getHCFSession(player.getUniqueId());

        if (!playerSession.getPvpClass().equals(PVPClass.ROGUE))
            return;

        if (!player.getItemInHand().getType().equals(Material.GOLD_SWORD))
            return;

        switch (targetSession.getPvpClass())
        {
            case BARD:
                event.setDamage(target.getMaxHealth() / 2);
                break;
            case ROGUE:
                event.setDamage(target.getMaxHealth() / 2);
                break;
            case ARCHER:
                event.setDamage(target.getMaxHealth() / 2);
                break;
            case DIAMOND:
                event.setDamage(target.getMaxHealth() / 4);
                break;
            default:
                return;
        }

        player.getItemInHand().setType(Material.AIR);
    }

    @EventHandler
    public void onInvisToggle(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR)
        {
            return;
        }

        if (event.getItem().getType() != Material.EYE_OF_ENDER)
        {
            return;
        }

        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.ROGUE.equals(session.getPvpClass()))
        {
            return;
        }

        if (invisCooldown.get(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendString(p, "classes.rogue.on-cooldown");
            return;
        }

        if (PlayerUtils.hasEffect(p, PotionEffectType.INVISIBILITY))
        {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        else
        {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, DesireHCF.getConfigHandler().getInteger
                    ("classes.rogue.invisible-length"), 1));
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (!event.hasItem() || event.getItem().getType().equals(Material.AIR))
        {
            return;
        }

        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.ROGUE.equals(session.getPvpClass()))
        {
            return;
        }

        ItemStack item = event.getItem();

        if (cooldown.get(p.getUniqueId()) != null)
        {
            if (ClassListener.isClassItem(item, PVPClass.ROGUE))
            {
                DesireHCF.getLangHandler().sendString(p, "classes.rogue.effect-cd");
            }
            return;
        }

        switch (item.getType())
        {
            case FEATHER:
                p.removePotionEffect(PotionEffectType.JUMP);
                PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, DesireHCF.getConfigHandler().getInteger("classes.rogue.effects.JUMP.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.rogue.effects.JUMP.click"));
                p.addPotionEffect(jump);
                break;
            case SUGAR:
                p.removePotionEffect(PotionEffectType.SPEED);
                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, DesireHCF.getConfigHandler().getInteger("classes.rogue.effects.SPEED.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.rogue.effects.SPEED.click"));
                p.addPotionEffect(speed);
                break;
            case IRON_INGOT:
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, DesireHCF.getConfigHandler().getInteger("classes.rogue.effects.RESISTANCE.duration") * 20,
                        DesireHCF.getConfigHandler().getInteger("classes.rogue.effects.RESISTANCE.click"));
                p.addPotionEffect(resistance);
                break;
        }

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
    }
}