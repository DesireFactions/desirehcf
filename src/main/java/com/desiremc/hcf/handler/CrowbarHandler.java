package com.desiremc.hcf.handler;

import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.util.FactionsUtils;
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

import java.util.Arrays;
import java.util.List;

public class CrowbarHandler implements Listener
{

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item == null || !isCrowbar(item))
        {
            return;
        }

        int uses = getUses(item);

        if (e.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            Block block = e.getClickedBlock();
            if (!(block.getType() == Material.MOB_SPAWNER || block.getType() == Material.ENDER_PORTAL_FRAME))
            {
                e.setCancelled(true);
                DesireHCF.getLangHandler().sendRenderMessage(player, "crowbar.wrong_block", true, false);
                return;
            }

            Faction target = FactionsUtils.getFaction(block.getLocation());
            Faction source = FactionsUtils.getFaction(player);

            if (target != source && !target.isWilderness())
            {
                e.setCancelled(true);
                DesireHCF.getLangHandler().sendRenderMessage(player, "crowbar.not_yours", true, false);
                return;
            }

            World world = block.getWorld();
            int cost;

            if (block.getType() == Material.MOB_SPAWNER)
            {
                cost = DesireHCF.getConfigHandler().getInteger("crowbar.spawner.cost");
                if (uses < cost)
                {
                    e.setCancelled(true);
                    DesireHCF.getLangHandler().sendRenderMessage(player, "crowbar.not_enough_charges", true, false);
                    return;
                }
                world.dropItemNaturally(block.getLocation(), getSpawner(((CreatureSpawner) block.getState()).getSpawnedType()));
            }
            else
            {
                cost = DesireHCF.getConfigHandler().getInteger("crowbar.frame.cost");
                if (uses < cost)
                {
                    e.setCancelled(true);
                    DesireHCF.getLangHandler().sendRenderMessage(player, "crowbar.not_enough_charges", true, false);
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
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1L, 1L);
                DesireHCF.getLangHandler().sendRenderMessage(player, "crowbar.out_of_uses", true, false);
            }

            Session s = SessionHandler.getSession(player);
            if (!s.hasAchievement(Achievement.FIRST_CROWBAR_USE))
            {
                s.awardAchievement(Achievement.FIRST_CROWBAR_USE, true);
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

    private static boolean isCrowbar(ItemStack is)
    {
        return is != null
                && is.getType() == Material.GOLD_HOE
                && is.hasItemMeta()
                && is.getItemMeta().hasDisplayName()
                && is.getItemMeta().getDisplayName().equals(DesireHCF.getLangHandler().renderMessage("crowbar.name", false, false))
                && is.getItemMeta().hasLore()
                && is.getItemMeta().getLore().size() >= 1
                && is.getItemMeta().getLore().get(0).startsWith(DesireHCF.getLangHandler().renderMessage("crowbar.prefix", false, false));
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
        return Arrays.asList(DesireHCF.getLangHandler().renderMessage("crowbar.prefix", false, false)
                + DesireHCF.getConfigHandler().getInteger("crowbar.uses"));
    }

    private static String getCrowbarName()
    {
        return DesireHCF.getLangHandler().renderMessage("crowbar.name", false, false);
    }

    public static int getUses(ItemStack is)
    {
        if (!isCrowbar(is))
        {
            return -1;
        }
        return Integer.parseInt(is.getItemMeta().getLore().get(0).replace(DesireHCF.getLangHandler().renderMessage("crowbar.prefix", false, false), ""));
    }

    public static ItemStack adjustUses(ItemStack is, int change)
    {
        if (isCrowbar(is))
        {
            int uses = getUses(is) + change;
            if (uses < 0)
            {
                throw new IllegalStateException("Can't have negative uses.");
            }
            List<String> lore = is.getItemMeta().getLore();
            lore.set(0, DesireHCF.getLangHandler().renderMessage("crowbar.prefix", false, false) + uses);

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
        meta.setDisplayName(DesireHCF.getLangHandler().renderMessage("crowbar.spawner", false, false) + type.toString());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isSpawner(ItemStack is)
    {
        return is.getType() == Material.MOB_SPAWNER && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(DesireHCF.getLangHandler().renderMessage("crowbar.spawner", false, false));
    }

    @SuppressWarnings("deprecation")
    public static EntityType getSpawnerType(ItemStack is)
    {
        String name = is.getItemMeta().getDisplayName();
        name = name.replace(DesireHCF.getLangHandler().renderMessage("crowbar.spawner", false, false), "");
        return EntityType.fromName(name);
    }

}
