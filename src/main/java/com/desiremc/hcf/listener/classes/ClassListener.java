package com.desiremc.hcf.listener.classes;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.event.ArmorEquipEvent;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassListener implements Listener
{

    private static FileHandler config;

    private static List<Material> archerItems;
    private static List<Material> bardItems;
    private static List<Material> minerItems;
    private static List<Material> rogueItems;

    public ClassListener()
    {
        config = DesireHCF.getConfigHandler();
    }

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

        archerItems = Arrays.asList(Material.FEATHER, Material.SUGAR);
        bardItems = Arrays.asList(Material.EYE_OF_ENDER, Material.BLAZE_POWDER, Material.GHAST_TEAR, Material.MAGMA_CREAM, Material.SUGAR,
                Material.IRON_AXE, Material.SPIDER_EYE, Material.FEATHER, Material.IRON_INGOT);
        rogueItems = Arrays.asList(Material.EYE_OF_ENDER, Material.FEATHER, Material.SUGAR, Material.IRON_INGOT);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        updateClass(player, player.getInventory().getHelmet());
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
                    applyPermanentEffects(PVPClass.DIAMOND, player);
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
                    applyPermanentEffects(PVPClass.BARD, player);
                }
                break;
            case "CHAINMAIL":
                if (isRogue(inv.getArmorContents()))
                {
                    session.setPvpClass(PVPClass.ROGUE);
                    applyPermanentEffects(PVPClass.ROGUE, player);
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

    public static void applyPermanentEffects(PVPClass pvpClass, Player player)
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
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
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
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                break;
            case ARCHER:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                break;
        }
    }

    public static void applyEffect(Player player, PotionEffectType type, String effectType, PVPClass pvpClass, int duration, int range, boolean faction, boolean self)
    {
        String location = "classes." + pvpClass.name() + ".effects." + type.getName() + "." + effectType;

        PotionEffect effect = new PotionEffect(type, duration, config.getInteger(location));

        List<Player> players;

        if (faction)
        {
            players = FactionsUtils.getFactionMembersInRange(player, range);
        }
        else
        {
            players = FactionsUtils.getNonFactionMembersInRange(player, range);
        }

        if (self)
        {
            players.add(player);
        }

        for (Player target : players)
        {
            target.addPotionEffect(effect);
        }
    }

    public static void applyEffectSelf(Player player, PotionEffectType type, String effectType, PVPClass pvpClass, int duration)
    {
        String location = "classes." + pvpClass.name() + ".effects." + type.getName() + "." + effectType;

        PotionEffect effect = new PotionEffect(type, duration, config.getInteger(location));

        player.addPotionEffect(effect);
    }

    public static boolean isClassItem(ItemStack item, PVPClass pvpClass)
    {
        if (item == null || item.getType().equals(Material.AIR))
        {
            return false;
        }

        switch (pvpClass.name())
        {
            case "ARCHER":
                return archerItems.contains(item.getType());
            case "BARD":
                return bardItems.contains(item.getType());
            case "MINER":
                return minerItems.contains(item.getType());
            case "ROGUE":
                return rogueItems.contains(item.getType());
            default:
                return false;
        }
    }
}