package com.desiremc.hcf.listener.factions;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.desiremc.core.events.PlayerBlockMoveEvent;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.core.utils.GeometryUtils;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.commands.factions.FactionHomeCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.ClaimSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionRelationship;
import com.desiremc.hcf.session.faction.FactionType;
import com.desiremc.hcf.util.FactionsUtils;
import com.desiremc.hcf.validators.SenderClaimHasPointOneValidator;
import com.desiremc.hcf.validators.SenderClaimingValidator;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.github.davidmoten.rtree.Entry;

/**
 * The listener in charge for ensuring that factions behave the way they are supposed to. It prevents players from going
 * places they shouldn't, announcing movement messages, and things of that nature.
 * 
 * @author Michael Ziluck
 */
public class PlayerListener implements Listener
{

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockMove(PlayerBlockMoveEvent event)
    {
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());
        Faction factionTo = FactionsUtils.getFaction(event.getTo());

        fSession.setLastLocation(factionTo);

        // cancel the home task if one exists
        BukkitTask task = FactionHomeCommand.getTeleportTask(fSession.getUniqueId());
        if (task != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(event.getPlayer(), "factions.home.cancelled", true, false);
            task.cancel();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        // handle everything to do with claiming.
        if (event.hasItem())
        {
            ItemStack item = event.getItem();
            if (FactionHandler.isClaimWand(item))
            {
                processClaim(fSession, event);
            }
        }

        // only look through things that can change things within a player's faction claim
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.PHYSICAL)
        {
            return;
        }

        Block block = event.getClickedBlock();

        // should never happen, but just in case.
        if (block == null)
        {
            return;
        }

        // handle boat glitching.
        if (fSession.getPlayer().getItemInHand() != null)
        {
            if (fSession.getPlayer().getItemInHand().getType() == Material.BOAT)
            {
                if (!playerCanUseItem(fSession, block.getLocation(), Material.BOAT))
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (!playerCanUseBlock(fSession, block))
        {
            event.setCancelled(true);
            return;
        }

        // we only care about right-clicks for the net check
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        if (!playerCanUseItem(fSession, block.getLocation(), event.getMaterial()))
        {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event)
    {
        Block block = event.getBlockClicked();
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        if (!playerCanUseItem(fSession, block.getLocation(), event.getBucket()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event)
    {
        Block block = event.getBlockClicked();
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        if (!playerCanUseItem(fSession, block.getLocation(), event.getBucket()))
        {
            event.setCancelled(true);
        }
    }

    /**
     * Processes the claim attempt being done by the player. The logic was moved out of the {@link EventHandler} to make
     * it from getting bloated.
     * 
     * @param fSession the person claiming land.
     * @param event the event that was fired.
     */
    private void processClaim(FSession fSession, PlayerInteractEvent event)
    {
        event.setCancelled(true);

        if (!new SenderHasFactionValidator().factionsValidate(fSession) // check that they're in a faction
                || !new SenderFactionOfficerValidator().factionsValidate(fSession) // check that they are a faction officer
                || !new SenderClaimingValidator().factionsValidate(fSession)) // check that they are currently claiming
        {
            FactionHandler.takeClaimWand(fSession.getPlayer());
            return;
        }

        Faction faction = fSession.getFaction();
        ClaimSession claim = fSession.getClaimSession();

        // the minimum size of the claim.
        int minSize = DesireHCF.getConfigHandler().getInteger("factions.claims.min_size");

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {

            Block block = event.getClickedBlock();
            BlockColumn blockColumn = new BlockColumn(block);

            if (!checkPoint(blockColumn, faction, fSession))
            {
                return;
            }

            // if they left click a block with the wand
            if (event.getAction() == Action.LEFT_CLICK_BLOCK)
            {
                if (claim.hasPointTwo())
                {
                    BoundedArea area = GeometryUtils.getBoundedArea(blockColumn, claim.getPointTwo());
                    if (area.getLength() < minSize || area.getWidth() < minSize)
                    {
                        DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.too_small", true, false,
                                "{size}", minSize);
                        return;
                    }
                    claim.setPointOne(blockColumn);
                    DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.cost_help", true, false,
                            "{x}", claim.getLength(),
                            "{z}", claim.getWidth(),
                            "{cost}", StringUtils.formatNumber(claim.getCost(), 2, true));
                    DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.confirm_help", true, false);
                }
                else
                {
                    claim.setPointOne(blockColumn);
                    DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.set.point_one", true, false);
                }
            }
            // if they right click a block with the wand
            else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                if (!new SenderClaimHasPointOneValidator().factionsValidate(fSession))
                {
                    return;
                }

                BoundedArea area = GeometryUtils.getBoundedArea(claim.getPointOne(), blockColumn);
                if (area.getLength() < minSize || area.getWidth() < minSize)
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.too_small", true, false,
                            "{size}", minSize);
                    return;
                }

                claim.setPointTwo(blockColumn);
                DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.cost_help", true, false,
                        "{x}", claim.getLength(),
                        "{z}", claim.getWidth(),
                        "{cost}", StringUtils.formatNumber(claim.getCost(), 2, true));
                DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.claims.confirm_help", true, false);
            }
        }
        
        
        // if they left click the air while sneaking
        if (event.getAction() == Action.LEFT_CLICK_AIR && fSession.getPlayer().isSneaking())
        {
            if (!claim.hasPointOne())
            {
                DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.need_point_one", true, false);
                return;
            }
            if (!claim.hasPointTwo())
            {
                DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.need_point_two", true, false);
                return;
            }
            if (faction.getBalance() < claim.getCost())
            {
                DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.too_poor", true, false);
                return;
            }
            if (!checkPoint(claim.getPointOne(), faction, fSession) || !checkPoint(claim.getPointTwo(), faction, fSession))
            {
                DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.error", true, false);
                fSession.clearClaimSession();
                return;
            }

            faction.addClaim(claim.getBoundedArea());

            fSession.clearClaimSession();

            faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.claims.complete", true, false,
                    "{player}", fSession.getName()));

        }
        else if (event.getAction() == Action.RIGHT_CLICK_AIR && fSession.getPlayer().isSneaking())
        {
            DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.cancel", true, false);
            fSession.clearClaimSession();
        }
    }

    private static boolean checkPoint(BlockColumn blockColumn, Faction faction, FSession fSession)
    {
        // all nearby claims
        Iterable<Entry<Faction, BoundedArea>> nearby = FactionHandler.getNearbyFactions(blockColumn, DesireHCF.getConfigHandler().getInteger("factions.claims.buffer"));
        for (Entry<Faction, BoundedArea> entry : nearby)
        {
            if (entry.value() != faction)
            {
                if (entry.geometry().intersects(blockColumn))
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.overlap.other", true, false);
                }
                else
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession, "factions.claims.too_close", true, false);
                }
                return false;
            }
            else
            {
                if (entry.geometry().intersects(blockColumn))
                {
                    DesireHCF.getLangHandler().sendRenderMessage(fSession, "faction.claims.overlap.self", true, false);
                    return false;
                }
                else if (entry.geometry().distance(blockColumn) == 1)
                {
                    return true;
                }
            }
        }
        if (faction.getClaims().size() != 0)
        {
            DesireHCF.getLangHandler().sendRenderMessage(fSession, "faction.claims.must_touch", true, false);
            return false;
        }
        return true;
    }

    private static final Set<Material> redstoneMaterials = EnumSet.of(
            Material.STONE_BUTTON,
            Material.WOOD_BUTTON,
            Material.WOODEN_DOOR,
            Material.WOOD_DOOR,
            Material.IRON_DOOR,
            Material.IRON_DOOR_BLOCK,
            Material.TRAP_DOOR,
            Material.LEVER,
            Material.GOLD_PLATE,
            Material.IRON_PLATE,
            Material.WOOD_PLATE,
            Material.STONE_PLATE,
            Material.FENCE_GATE,
            Material.TRIPWIRE,
            Material.TRIPWIRE_HOOK,
            Material.BED,
            Material.BED_BLOCK,
            Material.DIODE_BLOCK_ON,
            Material.DIODE_BLOCK_OFF,
            Material.SOIL);

    private static final Set<Material> chestMaterial = EnumSet.of(
            Material.CHEST,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST,
            Material.DISPENSER,
            Material.NOTE_BLOCK,
            Material.ENCHANTMENT_TABLE,
            Material.WORKBENCH,
            Material.FURNACE,
            Material.BURNING_FURNACE,
            Material.JUKEBOX,
            Material.BREWING_STAND,
            Material.BREWING_STAND_ITEM,
            Material.BEACON,
            Material.HOPPER,
            Material.HOPPER_MINECART,
            Material.STORAGE_MINECART,
            Material.POWERED_MINECART,
            Material.CAULDRON);

    public static boolean playerCanUseBlock(FSession fSession, Block block)
    {
        // if the player is in bypass mode, they can do anything.
        if (FactionHandler.isBypassing(fSession))
        {
            return true;
        }

        Faction otherFaction = FactionsUtils.getFaction(block.getLocation());

        // does not protect doors in warzone/safezone
        // also if the faction is raidable
        if (!otherFaction.isNormal() || otherFaction.isRaidable())
        {
            return true;
        }

        if (redstoneMaterials.contains(block.getType()))
        {
            if (otherFaction.getRelationshipTo(fSession.getFaction()).canUseRedstone())
            {
                return true;
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.protection.use_blocks", true, false);
                return false;
            }
        }

        if (chestMaterial.contains(block.getType()))
        {
            if (otherFaction.getRelationshipTo(fSession.getFaction()).canUseChests())
            {
                return true;
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.protection.use_chests", true, false);
                return false;
            }
        }

        return true;
    }

    protected static final Set<Material> useItems = EnumSet.of(Material.FIREBALL,
            Material.FLINT_AND_STEEL,
            Material.BUCKET,
            Material.WATER_BUCKET,
            Material.LAVA_BUCKET);

    public static boolean playerCanUseItem(FSession fSession, Location location, Material material)
    {
        // if the player is in bypass mode, they can do anything.
        if (FactionHandler.isBypassing(fSession))
        {
            return true;
        }

        Faction otherFaction = FactionsUtils.getFaction(location);

        // if the faction is raidable or they're a member, they can do stuff.
        if (otherFaction.isRaidable() || otherFaction == fSession.getFaction())
        {
            return true;
        }

        // only continue if we care
        if (!useItems.contains(material))
        {
            return true;
        }

        // safezones and warzones behave the same as far as item usage goes.
        if (otherFaction.getType() == FactionType.SAFEZONE || otherFaction.getType() == FactionType.WARZONE)
        {
            DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.protection.use_items", true, false);
            return false;
        }

        // wilderness you can do anything
        else if (otherFaction.getType() == FactionType.WILDERNESS)
        {
            return true;
        }

        // relationship counseling
        FactionRelationship rel = otherFaction.getRelationshipTo(fSession.getFaction());
        if (!rel.canBuild())
        {
            DesireHCF.getLangHandler().sendRenderMessage(fSession.getSession(), "factions.protection.use_items", true, false);
            return false;
        }

        return true;
    }

}
