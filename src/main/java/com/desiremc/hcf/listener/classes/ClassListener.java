package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.event.ArmorEquipEvent;
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
        updateClass(event.getPlayer());
    }

    private void updateClass(Player player)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(player.getUniqueId());

        PlayerInventory inv = player.getInventory();

        ItemStack helmet = inv.getHelmet();

        removePermanentEffects(session.getPvpClass(), player);

        switch (helmet.getType())
        {
            case DIAMOND_HELMET:
                if (isDiamond(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.DIAMOND);
                }
                break;
            case LEATHER_HELMET:
                if (isArcher(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.ARCHER);
                    applyPermanentEffects(PVPClass.ARCHER, player);
                }
                break;
            case GOLD_HELMET:
                if (isBard(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.BARD);
                }
                break;
            case CHAINMAIL_HELMET:
                if (isRogue(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.ROGUE);
                }
                break;
            case IRON_HELMET:
                if (isMiner(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.MINER);
                    applyPermanentEffects(PVPClass.MINER, player);

                    List<Integer> indexs = new ArrayList<>();

                    for(String temp : DesireHCF.getConfigHandler().getConfigurationSection("classes.archer.diamonds").getKeys(false))
                    {
                        indexs.add(Integer.valueOf(temp));
                    }

                    indexs.removeIf(integer -> integer > session.getDiamonds());

                    if(indexs.size() == 0)
                        return;

                    for(String info : DesireHCF.getConfigHandler().getConfigurationSection("classes.archer.diamonds" + indexs.get(indexs.size() - 1)).getKeys(false))
                    {
                        PotionEffect effect = new PotionEffect(PotionEffectType.getByName(info.split("-")[0]), Integer.MAX_VALUE, Integer.valueOf(info.split("-")[1]));
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
                return false;
        }
        return true;
    }

    private boolean isArcher(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("LEATHER"))
                return false;
        }
        return true;
    }

    private boolean isBard(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("GOLDEN"))
                return false;
        }
        return true;
    }

    private boolean isRogue(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("CHAINMAIL"))
                return false;
        }
        return true;
    }

    private boolean isMiner(ItemStack[] armor)
    {
        for (ItemStack item : armor)
        {
            if (!item.getType().name().contains("IRON"))
                return false;
        }
        return true;
    }

    private void applyPermanentEffects(PVPClass pvpClass, Player player)
    {
        switch (pvpClass)
        {
            case BARD:

                break;
            case MINER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                break;
            case ROGUE:

                break;
            case ARCHER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
                break;
            case DIAMOND:

                break;
        }
    }

    private void removePermanentEffects(PVPClass pvpClass, Player player)
    {
        switch (pvpClass)
        {
            case BARD:

                break;
            case MINER:
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.SPEED);
                break;
            case ROGUE:

                break;
            case ARCHER:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                break;
            case DIAMOND:

                break;
        }
    }
}