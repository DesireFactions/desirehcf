package com.desiremc.hcf.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.util.FactionsUtils;
import com.massivecraft.factions.Faction;

public class CrowbarHandler implements Listener
{

    private static final String PREFIX = "§aUses: §b";
    private static final String NAME = "§a§k|§cCrowbar§a§k|";
    private static final String SPAWNER = "§a§k|§cSpawner§a§k|§b - §a";

    private LangHandler lang = DesireHCF.getLangHandler();
    private FileHandler config = DesireHCF.getConfigHandler();

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item == null || !isCrowbar(item)) { return; }

        int uses = getUses(item);

        if (e.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            Block block = e.getClickedBlock();
            Faction target = FactionsUtils.getFaction(block.getLocation());
            Faction source = FactionsUtils.getFaction(player);
            if (target != null && target != source)
            {
                e.setCancelled(true);
                lang.sendString(player, "crowbar.not_yours");
                return;
            }
            if (!(block.getType() == Material.MOB_SPAWNER || block.getType() == Material.ENDER_PORTAL_FRAME))
            {
                e.setCancelled(true);
                lang.sendString(player, "crowbar.wrong_block");
                return;
            }

            World world = block.getWorld();
            int cost;

            if (block.getType() == Material.MOB_SPAWNER)
            {
                cost = config.getInteger("crowbar.spawner.cost");
                if (uses < cost)
                {
                    e.setCancelled(true);
                    lang.sendString(player, "crowbar.not_enough_charges");
                    return;
                }
                world.dropItemNaturally(block.getLocation(), getSpawner(((CreatureSpawner) block.getState()).getSpawnedType()));
            }
            else
            {
                cost = config.getInteger("crowbar.frame.cost");
                if (uses < cost)
                {
                    e.setCancelled(true);
                    lang.sendString(player, "crowbar.not_enough_charges");
                    return;
                }
                world.dropItemNaturally(block.getLocation(), new ItemStack(Material.ENDER_PORTAL_FRAME));
            }

            adjustUses(item, -cost);
            player.playSound(block.getLocation(), Sound.CLICK, 1, 1);
            block.setType(Material.AIR);

            if (getUses(item) <= 0)
            {
                player.setItemInHand(new ItemStack(Material.AIR));
                lang.sendString(player, "crowbar.out_of_uses");
            }
        }

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e)
    {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        ItemStack item = p.getItemInHand();

        if (isSpawner(item))
        {
            CreatureSpawner cs = (CreatureSpawner) b.getState();
            cs.setSpawnedType(getSpawnerType(item));
            cs.update();
        }
    }

    public static boolean isCrowbar(ItemStack is)
    {
        return is != null && is.getType() == Material.GOLD_HOE && is.getItemMeta().getDisplayName().equals(NAME) && is.getItemMeta().hasLore() && is.getItemMeta().getLore().size() >= 1 && is.getItemMeta().getLore().get(0).startsWith(PREFIX);
    }

    public static ItemStack getNewCrowbar()
    {
        ItemStack item = new ItemStack(Material.GOLD_HOE, 1);

        ItemMeta meta = item.getItemMeta();

        meta.setLore(getStartingUses());
        meta.setDisplayName(getCrowbarName());

        item.setItemMeta(meta);

        return item;
    }

    private static List<String> getStartingUses()
    {
        return Arrays.asList(PREFIX + DesireHCF.getConfigHandler().getInteger("crowbar.uses"));
    }

    private static String getCrowbarName()
    {
        return NAME;
    }

    public static int getUses(ItemStack is)
    {
        if (!isCrowbar(is)) { return -1; }
        return Integer.parseInt(is.getItemMeta().getLore().get(0).replace(PREFIX, ""));
    }

    public static ItemStack adjustUses(ItemStack is, int change)
    {
        if (isCrowbar(is))
        {
            int uses = getUses(is) + change;
            if (uses < 0) { throw new IllegalStateException("Can't have negative uses."); }
            List<String> lore = is.getItemMeta().getLore();
            lore.set(0, PREFIX + uses);

            ItemMeta meta = is.getItemMeta();
            meta.setLore(lore);
            is.setItemMeta(meta);
        }
        return is;
    }

    public static ItemStack getSpawner(EntityType type)
    {
        ItemStack item = new ItemStack(Material.MOB_SPAWNER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SPAWNER + type.toString());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isSpawner(ItemStack is)
    {
        return is.getType() == Material.MOB_SPAWNER && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(SPAWNER);
    }

    @SuppressWarnings("deprecation")
    public static EntityType getSpawnerType(ItemStack is)
    {
        String name = is.getItemMeta().getDisplayName();
        name = name.replace(SPAWNER, "");
        return EntityType.fromName(name);
    }

}
