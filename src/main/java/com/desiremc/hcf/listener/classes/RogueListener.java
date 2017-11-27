package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.PVPClass;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.listener.MovementListener;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RogueListener implements DesireClass
{

    public static Cache<UUID, Long> invisCooldown;

    public static List<UUID> hiddenPlayers;

    public RogueListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        invisCooldown = new Cache<>(DesireHCF.getConfigHandler().getInteger("classes.rogue.uninvis-timer"), TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
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

        hiddenPlayers = new ArrayList<>();

        Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : hiddenPlayers)
                {
                    Player player = PlayerUtils.getPlayer(uuid);

                    for (Player target : FactionsUtils.getEnemiesInRange(player, 10))
                    {
                        PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, null);
                        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
                        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
                        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
                        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);

                        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(handPacket);
                        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(helmetPacket);
                        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(chestPacket);
                        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(legPacket);
                        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(bootsPacket);
                    }
                }
            }
        }, 0, 20L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        if (!MovementListener.differentBlocks(event.getFrom(), event.getTo()))
        {
            return;
        }
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.ROGUE.equals(session.getPvpClass()))
        {
            return;
        }

        if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY))
        {
            return;
        }

        if (FactionsUtils.getEnemiesInRange(p, DesireHCF.getConfigHandler().getInteger("classes.rogue" + ".uninvis-range")).size() > 0)
        {
            invisCooldown.put(p.getUniqueId(), System.currentTimeMillis());
            DesireHCF.getLangHandler().sendString(p, "classes.rogue.shown");
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
        {
            return;
        }
        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        Player target = (Player) event.getEntity();
        Player player = (Player) event.getDamager();

        Vector attackerDirection = player.getLocation().getDirection();
        Vector victimDirection = target.getLocation().getDirection();

        if (!(attackerDirection.dot(victimDirection) > 0))
        {
            return;
        }

        HCFSession targetSession = HCFSessionHandler.getHCFSession(target.getUniqueId());
        HCFSession playerSession = HCFSessionHandler.getHCFSession(player.getUniqueId());

        if (playerSession.getPvpClass() == null || !playerSession.getPvpClass().equals(PVPClass.ROGUE))
        {
            return;
        }

        if (!player.getItemInHand().getType().equals(Material.GOLD_SWORD))
        {
            return;
        }

        if (targetSession.getPvpClass() == null)
        {
            return;
        }

        switch (targetSession.getPvpClass())
        {
            case BARD:
                event.setDamage(target.getMaxHealth() / 3);
                break;
            case ROGUE:
                event.setDamage(target.getMaxHealth() / 3);
                break;
            case ARCHER:
                event.setDamage(target.getMaxHealth() / 3);
                break;
            case DIAMOND:
                event.setDamage(target.getMaxHealth() / 5);
                break;
            default:
                return;
        }

        if (player.getItemInHand().getAmount() > 1)
        {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }
        else
        {
            player.getInventory().remove(player.getItemInHand());
        }
        player.updateInventory();
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

        if (p.hasPotionEffect(PotionEffectType.INVISIBILITY))
        {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        else
        {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, DesireHCF.getConfigHandler().getInteger("classes.rogue.invisible-length") * 20, 0));

            if (event.getItem().getAmount() > 1)
            {
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
            else
            {
                p.getInventory().remove(event.getItem());
            }
            p.updateInventory();
        }
    }
}