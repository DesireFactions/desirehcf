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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArcherListener implements DesireClass
{

    private Cache<UUID, UUID> archerHit;
    private Cache<UUID, Long> cooldown;

    private int duration;

    public ArcherListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        duration = DesireHCF.getConfigHandler().getInteger("classes.archer.duration") * 20;

        archerHit = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.archer.hit-time"), TimeUnit.SECONDS,
                new RemovalListener<UUID, UUID>()
                {
                    @Override
                    public void onRemoval(RemovalNotification<UUID, UUID> entry)
                    {
                        if (entry.getCause() != RemovalNotification.Cause.EXPIRE)
                        {
                            return;
                        }
                        Player p = PlayerUtils.getPlayer(entry.getKey());
                        if (p != null)
                        {
                            DesireHCF.getLangHandler().sendString(p, "classes.archer.hit-off");
                        }
                    }
                }, DesireHCF.getInstance());

        cooldown = new Cache<>(duration / 20, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.archer.effects-over");

                    HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());
                    if (PVPClass.ARCHER.equals(session.getPvpClass()))
                    {
                        ClassListener.applyPermanentEffects(PVPClass.ARCHER, p);
                    }
                }
            }
        }, DesireHCF.getInstance());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
        {
            return;
        }

        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        Player source = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (archerHit.get(target.getUniqueId()) == null)
        {
            return;
        }

        if (FactionsUtils.getFaction(source) == FactionsUtils.getFaction(archerHit.get(target.getUniqueId())))
        {
            return;
        }

        event.setDamage(event.getDamage() * DesireHCF.getConfigHandler().getDouble("classes.archer.increase-percent"));
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event)
    {

        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        if (!(event.getDamager() instanceof Arrow))
        {
            return;
        }

        Arrow pj = (Arrow) event.getDamager();

        if (!(pj.getShooter() instanceof Player))
        {
            return;
        }

        Player target = (Player) event.getEntity();
        Player source = (Player) pj.getShooter();

        HCFSession sourceSession = HCFSessionHandler.getHCFSession(source.getUniqueId());

        if (!PVPClass.ARCHER.equals(sourceSession.getPvpClass()))
        {
            return;
        }

        int range = DesireHCF.getConfigHandler().getInteger("classes.archer.range");

        if (source.getLocation().distanceSquared(target.getLocation()) < (range * range))
        {
            return;
        }

        if (archerHit.get(target.getUniqueId()) != null)
        {
            archerHit.put(target.getUniqueId(), source.getUniqueId());
        }
        else
        {
            archerHit.replace(target.getUniqueId(), source.getUniqueId());
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

        ItemStack item = event.getItem();

        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.ARCHER.equals(session.getPvpClass()))
        {
            return;
        }

        if (!ClassListener.isClassItem(item, PVPClass.ARCHER))
        {
            return;
        }

        if (cooldown.get(p.getUniqueId()) != null)
        {
            //cooldown message
            return;
        }

        switch (item.getType())
        {
            case FEATHER:
                p.removePotionEffect(PotionEffectType.JUMP);
                ClassListener.applyEffectSelf(p, PotionEffectType.JUMP, "click", PVPClass.ARCHER, duration);
                break;
            case SUGAR:
                p.removePotionEffect(PotionEffectType.SPEED);
                ClassListener.applyEffectSelf(p, PotionEffectType.SPEED, "click", PVPClass.ARCHER, duration);
                break;
        }

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void playerItemHeldEvent(PlayerItemHeldEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.ARCHER.equals(session.getPvpClass()))
        {
            return;
        }

        ItemStack item = p.getInventory().getItem(event.getNewSlot());

        if (item == null || item.getType().equals(Material.AIR))
        {
            return;
        }
        applyEffect(p, item);
    }

    private void applyEffect(Player p, ItemStack item)
    {
        switch (item.getType())
        {
            case FEATHER:
                ClassListener.applyEffectSelf(p, PotionEffectType.JUMP, "hold", PVPClass.ARCHER, duration);
                break;
        }
    }
}
