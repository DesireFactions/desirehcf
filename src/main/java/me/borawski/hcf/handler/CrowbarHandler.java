package me.borawski.hcf.handler;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
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

import com.massivecraft.factions.Faction;

import me.borawski.hcf.Core;
import me.borawski.hcf.MscAchievements;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.CrowbarUtils;
import me.borawski.hcf.util.FactionsUtils;
import me.borawski.hcf.util.Utils;

public class CrowbarHandler implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getItemInHand();
        Action action = event.getAction();
        if (item == null) {
            return;
        }
        if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Block clickedBlock = event.getClickedBlock();
            World world = clickedBlock.getWorld();
            Location location = clickedBlock.getLocation();
            if (CrowbarUtils.isCrowbar(item)) {
                event.setCancelled(true);
                Faction faction = FactionsUtils.getFaction(player);
                Faction faction2 = FactionsUtils.getFaction(clickedBlock.getLocation());
                if (faction != faction2) {
                    return;
                }
                if (clickedBlock.getType().equals(Material.MOB_SPAWNER)) {
                    int uses = CrowbarUtils.getUses(item, Utils.chat(Core.getInstance().getConfig().getString("crowbar-spawners-string") + Core.getInstance().getConfig().getString("crowbar-spawners-uses-string")));
                    int uses2 = CrowbarUtils.getUses(item, Utils.chat(Core.getInstance().getConfig().getString("crowbar-portals-string") + Core.getInstance().getConfig().getString("crowbar-portals-uses-string")));
                    if (uses > 0) {
                        Session s = SessionHandler.getSession(event.getPlayer());
                        world.playEffect(location, Effect.STEP_SOUND, Material.MOB_SPAWNER.getId());
                        ItemStack setItemName = CrowbarUtils.setItemName(new ItemStack(Material.MOB_SPAWNER), String.valueOf(Core.getInstance().getConfig().getString("crowbar-spawner-name-color")) + ((CreatureSpawner) clickedBlock.getState()).getSpawnedType().name() + " Spawner");
                        clickedBlock.setType(Material.AIR);
                        world.dropItemNaturally(location, setItemName);
                        if (--uses <= 0 && uses2 <= 0) {
                            player.setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            CrowbarUtils.updateOrCreateCrowbarMeta(item, uses, uses2);
                        }
                        if (!s.hasAchievement("first_use_crowbar")) {
                            s.awardAchievement(MscAchievements.FIRST_USE_CROWBAR, true);
                        }
                        player.updateInventory();
                    } else {
                        player.sendMessage("Crowbar has zero uses for spawners!");
                    }
                } else if (clickedBlock.getType().equals(Material.ENDER_PORTAL_FRAME)) {
                    int uses3 = CrowbarUtils.getUses(item, Utils.chat(Core.getInstance().getConfig().getString("crowbar-spawners-string") + Core.getInstance().getConfig().getString("crowbar-spawners-uses-string")));
                    int uses4 = CrowbarUtils.getUses(item, Utils.chat(Core.getInstance().getConfig().getString("crowbar-portals-string") + Core.getInstance().getConfig().getString("crowbar-portals-uses-string")));
                    if (uses4 > 0) {
                        clickedBlock.setType(Material.AIR);
                        world.playEffect(location, Effect.STEP_SOUND, Material.ENDER_PORTAL_FRAME.getId());
                        world.dropItemNaturally(location, new ItemStack(Material.ENDER_PORTAL_FRAME));
                        if (--uses4 <= 0 && uses3 <= 0) {
                            player.setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            CrowbarUtils.updateOrCreateCrowbarMeta(item, uses3, uses4);
                        }
                        player.updateInventory();
                    } else {
                        player.sendMessage("Crowbar has zero uses for portals!");
                    }
                } else {
                    player.sendMessage("Spawners and Portal Frames only!");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Block block = blockPlaceEvent.getBlock();
        ItemStack itemInHand = blockPlaceEvent.getItemInHand();
        String displayName = itemInHand.getItemMeta().getDisplayName();
        if (itemInHand.getType().equals(Material.MOB_SPAWNER) && itemInHand.getItemMeta().hasDisplayName() && displayName.startsWith(Core.getInstance().getConfig().getString("crowbar-spawner-name-color")) && displayName.endsWith(" Spawner")) {
            EntityType value = EntityType.valueOf(ChatColor.stripColor(displayName).replace(" Spawner", "").replace(" ", "_").toUpperCase());
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            creatureSpawner.setSpawnedType(value);
            creatureSpawner.update();
        }
    }
}
