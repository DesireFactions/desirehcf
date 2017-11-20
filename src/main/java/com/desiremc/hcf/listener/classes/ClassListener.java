package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.PVPClass;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.event.ArmorEquipEvent;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ClassListener implements Listener
{

    @EventHandler
    public void onArmorChange(ArmorEquipEvent event)
    {
        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                updateClass(event.getPlayer(), event.getNewArmorPiece());
            }
        }, 2L);
    }

    private void updateClass(Player player, ItemStack item)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(player.getUniqueId());

        PlayerInventory inv = player.getInventory();

        if (session.getPvpClass() != null)
        {
            removePermanentEffects(session.getPvpClass(), player);
        }

        if (item == null || item.getType().equals(Material.AIR))
        {
            session.setPvpClass(null);
            return;
        }

        switch (item.getType().name().split("_")[0])
        {
            case "DIAMOND":
                if (isDiamond(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.DIAMOND);
                }
                break;
            case "LEATHER":
                if (isArcher(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.ARCHER);
                    applyPermanentEffects(PVPClass.ARCHER, player);
                }
                break;
            case "GOLD":
                if (isBard(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.BARD);
                }
                break;
            case "CHAINMAIL":
                if (isRogue(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.ROGUE);
                }
                break;
            case "IRON":
                if (isMiner(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.MINER);
                    applyPermanentEffects(PVPClass.MINER, player);

                    List<Integer> indexs = new ArrayList<>();

                    for (String temp : DesireHCF.getConfigHandler().getConfigurationSection("classes.miner" + ".diamonds").getKeys(false))
                    {
                        indexs.add(Integer.valueOf(temp));
                    }

                    indexs.removeIf(integer -> integer > session.getCurrentOre().getDiamondCount());

                    if (indexs.size() == 0)
                    {
                        return;
                    }

                    ConfigurationSection cs = DesireHCF.getConfigHandler().getConfigurationSection("classes.miner.diamonds." + indexs.get(indexs.size() - 1));
                    for (String info : cs.getKeys(false))
                    {
                        PotionEffect effect = new PotionEffect(PotionEffectType.getByName(info), Integer.MAX_VALUE, cs.getInt(info) - 1);
                        player.addPotionEffect(effect);
                    }
                }
                break;
        }

    }

    private boolean isDiamond(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("DIAMOND"))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isArcher(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("LEATHER"))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isBard(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("GOLD"))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isRogue(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("CHAINMAIL"))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isMiner(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("IRON"))
            {
                return false;
            }
        }
        return true;
    }

    private void applyPermanentEffects(PVPClass pvpClass, Player player)
    {
        switch (pvpClass)
        {
            case BARD:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                break;
            case MINER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                break;
            case ROGUE:
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                break;
            case ARCHER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                break;
        }
    }

    private void removePermanentEffects(PVPClass pvpClass, Player player)
    {
        switch (pvpClass)
        {
            case BARD:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                break;
            case MINER:
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.SPEED);
                break;
            case ROGUE:
                player.removePotionEffect(PotionEffectType.JUMP);
                player.removePotionEffect(PotionEffectType.SPEED);
                break;
            case ARCHER:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                break;
        }
    }
}