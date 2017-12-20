package com.desiremc.hcf.listener.classes;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.events.ArmorEquipEvent;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClassListener implements Listener
{

    private static FileHandler config;

    private static List<Material> archerItems;
    private static List<Material> bardItems;
    private static List<Material> rogueItems;

    private static HashMap<UUID, Long> holders;

    private static HashMap<UUID, Integer> energy;

    public ClassListener()
    {
        config = DesireHCF.getConfigHandler();
        energy = new HashMap<>();

        archerItems = Arrays.asList(Material.FEATHER, Material.SUGAR);
        bardItems = Arrays.asList(Material.EYE_OF_ENDER, Material.BLAZE_POWDER, Material.GHAST_TEAR, Material.MAGMA_CREAM, Material.SUGAR,
                Material.IRON_AXE, Material.SPIDER_EYE, Material.FEATHER, Material.IRON_INGOT);
        rogueItems = Arrays.asList(Material.EYE_OF_ENDER, Material.FEATHER, Material.SUGAR, Material.IRON_INGOT);

        holders = new HashMap<>();

        Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {

                for (UUID uuid : energy.keySet())
                {
                    Player p = PlayerUtils.getPlayer(uuid);
                    if (p != null)
                    {
                        if (energy.get(uuid) == 100)
                        {
                            continue;
                        }

                        int newEnergy = energy.get(uuid) + 1;
                        energy.replace(uuid, newEnergy);

                        EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().getStringNoPrefix("classes.energy-scoreboard"),
                                String.valueOf(newEnergy));
                    }
                }
            }
        }, 0, 20L);

        Bukkit.getScheduler().runTaskTimer(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : holders.keySet())
                {
                    Player p = PlayerUtils.getPlayer(uuid);
                    if (p == null)
                    {
                        return;
                    }

                    if (holders.get(uuid) <= System.currentTimeMillis())
                    {
                        holdEvent(p, p.getItemInHand());
                    }
                }
            }
        }, 0, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        holders.remove(event.getPlayer().getUniqueId());
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
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        updateClass(player, player.getInventory().getHelmet());
    }

    public static void updateClass(Player player, ItemStack item)
    {
        FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());

        PlayerInventory inv = player.getInventory();

        if (session.getPvpClass() != null)
        {
            removePermanentEffects(session.getPvpClass(), player);
            DesireHCF.getLangHandler().sendRenderMessageNoPrefix(player, "classes.disable", "{class}", StringUtils.capitalize(session.getPvpClass().name().toLowerCase()));
            EntryRegistry.getInstance().removeValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.scoreboard"));
            EntryRegistry.getInstance().removeValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.energy-scoreboard"));
        }

        boolean set = false;

        switch (item.getType().name().split("_")[0])
        {
            case "DIAMOND":
                if (isDiamond(inv.getArmorContents()))
                {
                    set = true;
                    session.setPvpClass(PVPClass.DIAMOND);
                    applyPermanentEffects(PVPClass.DIAMOND, player);
                }
                break;
            case "LEATHER":
                if (isArcher(inv.getArmorContents()))
                {
                    set = true;
                    session.setPvpClass(PVPClass.ARCHER);
                    applyPermanentEffects(PVPClass.ARCHER, player);

                    DesireHCF.getLangHandler().sendRenderMessageNoPrefix(player, "classes.enable", "{class}", "Archer");
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.scoreboard"), "Archer");
                    energy.put(player.getUniqueId(), 0);
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.energy-scoreboard"), "0");

                    Session s = SessionHandler.getSession(player);
                    if (!s.hasAchievement(Achievement.FIRST_ARCHER))
                    {
                        s.awardAchievement(Achievement.FIRST_ARCHER, true);
                    }
                }
                break;
            case "GOLD":
                if (isBard(inv.getArmorContents()))
                {
                    set = true;
                    session.setPvpClass(PVPClass.BARD);
                    applyPermanentEffects(PVPClass.BARD, player);

                    DesireHCF.getLangHandler().sendRenderMessageNoPrefix(player, "classes.enable", "{class}", "Bard");
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.scoreboard"), "Bard");
                    energy.put(player.getUniqueId(), 0);
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.energy-scoreboard"), "0");

                    Session s = SessionHandler.getSession(player);
                    if (!s.hasAchievement(Achievement.FIRST_BARD))
                    {
                        s.awardAchievement(Achievement.FIRST_BARD, true);
                    }
                }
                break;
            case "CHAINMAIL":
                if (isRogue(inv.getArmorContents()))
                {
                    set = true;
                    session.setPvpClass(PVPClass.ROGUE);
                    applyPermanentEffects(PVPClass.ROGUE, player);

                    DesireHCF.getLangHandler().sendRenderMessageNoPrefix(player, "classes.enable", "{class}", "Rogue");
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.scoreboard"), "Rogue");
                    energy.put(player.getUniqueId(), 0);
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.energy-scoreboard"), "0");

                    Session s = SessionHandler.getSession(player);
                    if (!s.hasAchievement(Achievement.FIRST_ROGUE))
                    {
                        s.awardAchievement(Achievement.FIRST_ROGUE, true);
                    }
                }
                break;
            case "IRON":
                if (isMiner(inv.getArmorContents()))
                {
                    set = true;
                    session.setPvpClass(PVPClass.MINER);
                    applyPermanentEffects(PVPClass.MINER, player);

                    DesireHCF.getLangHandler().sendRenderMessageNoPrefix(player, "classes.enable", "{class}", "Miner");
                    EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.scoreboard"), "Miner");

                    Session s = SessionHandler.getSession(player);
                    if (!s.hasAchievement(Achievement.FIRST_MINER))
                    {
                        s.awardAchievement(Achievement.FIRST_MINER, true);
                    }

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

        if (!set)
        {
            energy.remove(player.getUniqueId());
            session.setPvpClass(null);
        }

    }

    private static boolean isDiamond(ItemStack[] armor)
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

    private static boolean isArcher(ItemStack[] armor)
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

    private static boolean isBard(ItemStack[] armor)
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

    private static boolean isRogue(ItemStack[] armor)
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

    private static boolean isMiner(ItemStack[] armor)
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
        if (pvpClass == null)
        {
            return;
        }

        switch (pvpClass)
        {
            case BARD:
                applyPotionEffect(player, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                break;
            case MINER:
                applyPotionEffect(player, new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                break;
            case ROGUE:
                applyPotionEffect(player, new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
                break;
            case ARCHER:
                applyPotionEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                applyPotionEffect(player, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                break;
        }
    }

    private static void removePermanentEffects(PVPClass pvpClass, Player player)
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
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                break;
            case ARCHER:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                break;
        }
    }

    public static void applyEffect(Player player, Material material, String effectType, PVPClass pvpClass, int duration, int range, boolean faction, boolean self, boolean allies, boolean other)
    {
        String location = "classes." + pvpClass.name().toLowerCase() + ".effects." + material.name() + "." + effectType;
        int amplifier = config.getInteger(location + ".amplifier");

        PotionEffect effect = new PotionEffect(PotionEffectType.getByName(config.getString("classes." + pvpClass.name().toLowerCase() + ".effects." + material.name() + ".effect")),
                duration * 20, amplifier);

        List<Player> players = new ArrayList<>();

        if (faction)
        {
            players.addAll(FactionsUtils.getFactionMembersInRange(player, range));
        }

        if (allies)
        {
            players.addAll(FactionsUtils.getAlliesInRange(player, range));
        }

        if (other)
        {
            players.addAll(FactionsUtils.getEnemiesInRange(player, range));
        }

        if (self)
        {
            players.add(player);
        }

        for (Player target : players)
        {
            if (!target.hasPotionEffect(effect.getType()) || PlayerUtils.getEffect(target, effect.getType()).getAmplifier() <= amplifier)
            {
                target.removePotionEffect(effect.getType());
                target.addPotionEffect(effect);
            }
        }

        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Player target : players)
                {
                    FSession sesh = FSessionHandler.getFSession(target);
                    applyPermanentEffects(sesh.getPvpClass(), target);
                }
            }
        }, (duration * 20) + 1);
    }

    public static void applyEffectSelf(Player player, Material material, String effectType, PVPClass pvpClass, int duration)
    {
        String location = "classes." + pvpClass.name().toLowerCase() + ".effects." + material.name() + "." + effectType;
        int amplifier = config.getInteger(location + ".amplifier");

        PotionEffect effect = new PotionEffect(PotionEffectType.getByName(config.getString("classes." + pvpClass.name().toLowerCase() + ".effects." + material.name() + ".effect")),
                duration * 20, amplifier);

        if (!player.hasPotionEffect(effect.getType()) || PlayerUtils.getEffect(player, effect.getType()).getAmplifier() <= amplifier)
        {
            player.removePotionEffect(effect.getType());
            player.addPotionEffect(effect);
        }

        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());
                if (session.getPvpClass() != null && pvpClass == session.getPvpClass())
                {
                    applyPermanentEffects(pvpClass, player);
                }
            }
        }, (duration * 20) + 1);
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
            case "ROGUE":
                return rogueItems.contains(item.getType());
            default:
                return false;
        }
    }

    private static void applyPotionEffect(Player player, PotionEffect effect)
    {
        if (player.hasPotionEffect(effect.getType()))
        {
            if (effect.getAmplifier() <= PlayerUtils.getEffect(player, effect.getType()).getAmplifier())
            {
                return;
            }
        }

        player.removePotionEffect(effect.getType());
        player.addPotionEffect(effect);
    }

    @EventHandler
    public void onMilkedUp(PlayerItemConsumeEvent event)
    {
        if (!event.getItem().getType().equals(Material.MILK_BUCKET))
        {
            return;
        }

        Player player = event.getPlayer();
        FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());

        if (session.getPvpClass() == null)
        {
            return;
        }

        Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                applyPermanentEffects(session.getPvpClass(), player);
            }
        }, 5L);
    }

    private static boolean isClassEvent(PVPClass pvpClass, FSession session, ItemStack item, String effectType)
    {
        if (session.getPvpClass() == null || !pvpClass.equals(session.getPvpClass()))
        {
            return false;
        }

        if (item == null || item.getType().equals(Material.AIR))
        {
            return false;
        }

        if (!isClassItem(item, pvpClass))
        {
            return false;
        }

        ConfigurationSection section = DesireHCF.getConfigHandler().getConfigurationSection("classes." + pvpClass.name().toLowerCase() + ".effects");

        return section.get(item.getType().name() + "." + effectType) != null;
    }

    @EventHandler
    public void onItemHoldEvent(PlayerItemHeldEvent event)
    {
        holdEvent(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    private void holdEvent(Player player, ItemStack item)
    {
        FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());

        if (session.getPvpClass() == null || !ClassListener.isClassEvent(session.getPvpClass(), session, item, "hold"))
        {
            ClassListener.holders.remove(player.getUniqueId());
            return;
        }

        ConfigurationSection section = DesireHCF.getConfigHandler().getConfigurationSection("classes." +
                session.getPvpClass().name().toLowerCase() + ".effects." + item.getType().name() + ".hold");

        int duration = section.getInt("duration");
        int range = DesireHCF.getConfigHandler().getInteger("classes.bard.range");

        if (PVPClass.ROGUE.equals(session.getPvpClass()) || PVPClass.ARCHER.equals(session.getPvpClass()))
        {
            ClassListener.applyEffectSelf(player, item.getType(), "hold", session.getPvpClass(), duration);
        }
        else
        {
            ClassListener.applyEffect(player, item.getType(), "hold", session.getPvpClass(), duration, range,
                    section.getBoolean("faction"), section.getBoolean("self"),
                    section.getBoolean("allies"),
                    section.getBoolean("other"));
        }

        ClassListener.holders.put(player.getUniqueId(), System.currentTimeMillis() + (duration * 1000) - 1000);
    }

    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        ItemStack item = event.getItem();
        FSession session = FSessionHandler.getOnlineFSession(player.getUniqueId());

        if (!ClassListener.isClassEvent(session.getPvpClass(), session, item, "click"))
        {
            return;
        }

        ConfigurationSection section = DesireHCF.getConfigHandler().getConfigurationSection("classes." +
                session.getPvpClass().name().toLowerCase() + ".effects." + item.getType().name() + ".click");

        int energyNeeded = section.getInt("energy");
        int duration = section.getInt("duration");
        int range = DesireHCF.getConfigHandler().getInteger("classes.bard.range");

        if (energy.get(player.getUniqueId()) < energyNeeded)
        {
            DesireHCF.getLangHandler().sendRenderMessage(player, "classes.not-enough-energy", "{amount}", energyNeeded);
            return;
        }

        int newEnergy = energy.get(player.getUniqueId()) - energyNeeded;

        energy.replace(player.getUniqueId(), newEnergy);
        EntryRegistry.getInstance().setValue(player, DesireHCF.getLangHandler().getStringNoPrefix("classes.energy-scoreboard"),
                String.valueOf(newEnergy));

        if (PVPClass.ROGUE.equals(session.getPvpClass()) || PVPClass.ARCHER.equals(session.getPvpClass()))
        {
            ClassListener.applyEffectSelf(player, item.getType(), "click", session.getPvpClass(), duration);
        }
        else
        {
            ClassListener.applyEffect(player, item.getType(), "click", session.getPvpClass(), duration, range,
                    section.getBoolean("faction"), section.getBoolean("self"),
                    section.getBoolean("allies"),
                    section.getBoolean("other"));
        }

        if (item.getAmount() > 1)
        {
            item.setAmount(player.getItemInHand().getAmount() - 1);
        }
        else
        {
            player.getInventory().remove(item);
        }
        player.updateInventory();
    }
}