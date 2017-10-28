package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RogueListener implements DesireClass
{

    private static Cache<UUID, Long> invisCooldown;

    @Override
    public void initialize()
    {
        invisCooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.rogue.uninvis-timer"), TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null)
                {
                    DesireHCF.getLangHandler().sendString(p, "classes.rogue.uninvis-over");
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

        if (FactionsUtils.getNonFactionMembersInRange(p, DesireHCF.getConfigHandler().getInteger("classes.rogue.uninvis-range")).size() > 0)
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
    public void onRightClick(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (event.getItem().getType() != Material.EYE_OF_ENDER)
            return;

        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!session.getPvpClass().equals(PVPClass.ROGUE))
            return;

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
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, DesireHCF.getConfigHandler().getInteger("classes.rogue.invisible-length"), 1));
        }
    }

    public static void caughtByBard(Player target)
    {
        invisCooldown.put(target.getUniqueId(), System.currentTimeMillis());
    }
}