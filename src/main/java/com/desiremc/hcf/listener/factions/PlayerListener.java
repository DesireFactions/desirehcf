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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.events.PlayerBlockMoveEvent;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.core.utils.BoundedArea;
import com.desiremc.core.utils.GeometryUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.commands.spawn.SpawnCommand;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockMove(PlayerBlockMoveEvent event)
    {
        HCFSession hcfSession = HCFSessionHandler.getHCFSession(event.getPlayer().getUniqueId());
        Faction factionTo = FactionsUtils.getFaction(event.getTo());

        hcfSession.setLastLocation(factionTo);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        HCFSession hcfSession = HCFSessionHandler.getHCFSession(event.getPlayer().getUniqueId());

        // handle everything to do with claiming.
        if (event.hasItem())
        {
            ItemStack item = event.getItem();
            if (FactionHandler.isClaimWand(item))
            {
                processClaim(hcfSession, event);
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
        if (hcfSession.getPlayer().getItemInHand() != null && hcfSession.getPlayer().getItemInHand().getType() == Material.BOAT)
        {
            if (!playerCanUseItem(hcfSession, block.getLocation(), Material.BOAT))
            {
                event.setCancelled(true);
                return;
            }
        }

        if (!playerCanUseBlock(hcfSession, block))
        {
            event.setCancelled(true);
            return;
        }

        // we only care about right-clicks for the net check
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        if (!playerCanUseItem(hcfSession, block.getLocation(), event.getMaterial()))
        {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event)
    {
        event.setRespawnLocation(SpawnCommand.spawnLocation);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event)
    {
        Block block = event.getBlockClicked();
        HCFSession hcfSession = HCFSessionHandler.getHCFSession(event.getPlayer().getUniqueId());

        if (!playerCanUseItem(hcfSession, block.getLocation(), event.getBucket()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event)
    {
        Block block = event.getBlockClicked();
        HCFSession hcfSession = HCFSessionHandler.getHCFSession(event.getPlayer().getUniqueId());

        if (!playerCanUseItem(hcfSession, block.getLocation(), event.getBucket()))
        {
            event.setCancelled(true);
        }
    }

    /**
     * Processes the claim attempt being done by the player. The logic was moved out of the {@link EventHandler} to make
     * it from getting bloated.
     * 
     * @param session the person claiming land.
     * @param event the event that was fired.
     */
    private void processClaim(HCFSession session, PlayerInteractEvent event)
    {
        if (!new SenderHasFactionValidator().factionsValidate(session) // check that they're in a faction
                || !new SenderFactionOfficerValidator().factionsValidate(session) // check that they are a faction officer
                || !new SenderClaimingValidator().factionsValidate(session)) // check that they are currently claiming
        {
            FactionHandler.takeClaimWand(session.getPlayer());
            event.setCancelled(true);
        }

        Faction faction = session.getFaction();
        ClaimSession claim = session.getClaimSession();
        Block block = event.getClickedBlock();
        BlockColumn blockColumn = new BlockColumn(block);

        // all nearby claims
        Iterable<Entry<Faction, BoundedArea>> nearby = FactionHandler.getNearbyFactions(blockColumn, DesireHCF.getConfigHandler().getInteger("factions.claims.buffer"));
        boolean valid = faction.getClaims().size() == 0;
        for (Entry<Faction, BoundedArea> entry : nearby)
        {
            if (entry.value() != faction)
            {
                if (entry.geometry().intersects(blockColumn))
                {
                    DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.overlap.other");
                }
                else
                {
                    DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.too_close");
                }
                return;
            }
            else
            {
                if (entry.geometry().intersects(blockColumn))
                {
                    DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "faction.claims.overlap.self");
                }
                else if (entry.geometry().distance(blockColumn) > 1)
                {
                    DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "faction.claims.must_touch");
                }
                else
                {
                    valid = true;
                }
            }
        }

        if (!valid)
        {
            return;
        }

        // the minimum size of the claim.
        int minSize = DesireHCF.getConfigHandler().getInteger("factions.claims.min_size");

        // if they left click a block with the wand
        if (event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            if (claim.hasPointTwo())
            {
                if (GeometryUtils.getArea(blockColumn, claim.getPointTwo()) < minSize * minSize)
                {
                    DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.too_small",
                            "{size}", minSize);
                    return;
                }
                DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.cost_help",
                        "{x}", claim.getLength(),
                        "{z}", claim.getWidth(),
                        "{cost}", claim.getCost());
                DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.confirm_help");
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.set.point_one");
            }
            claim.setPointOne(blockColumn);
        }
        // if they right click a block with the wand
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (!new SenderClaimHasPointOneValidator().factionsValidate(session))
            {
                return;
            }

            if (claim.getArea() < minSize * minSize)
            {
                DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.too_small",
                        "{size}", minSize);
                return;
            }

            DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.cost_help",
                    "{x}", claim.getLength(),
                    "{z}", claim.getWidth(),
                    "{cost}", claim.getCost());
            DesireHCF.getLangHandler().sendRenderMessage(session.getSession(), "factions.claims.confirm_help");
            claim.setPointTwo(blockColumn);
        }
        // if they left click the air while sneaking
        else if (event.getAction() == Action.LEFT_CLICK_AIR && session.getPlayer().isSneaking())
        {
            // TODO check the claim
        }

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

    public static boolean playerCanUseBlock(HCFSession hcfSession, Block block)
    {
        // if the player is in bypass mode, they can do anything.
        if (FactionHandler.isBypassing(hcfSession))
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
            if (otherFaction.getRelationshipTo(hcfSession.getFaction()).canUseRedstone())
            {
                return true;
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(hcfSession.getSession(), "factions.protection.use_blocks");
                return false;
            }
        }

        if (chestMaterial.contains(block.getType()))
        {
            if (otherFaction.getRelationshipTo(hcfSession.getFaction()).canUseChests())
            {
                return true;
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(hcfSession.getSession(), "factions.protection.use_chests");
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

    public static boolean playerCanUseItem(HCFSession hcfSession, Location location, Material material)
    {
        // if the player is in bypass mode, they can do anything.
        if (FactionHandler.isBypassing(hcfSession))
        {
            return true;
        }

        Faction otherFaction = FactionsUtils.getFaction(location);

        // if the faction is raidable or they're a member, they can do stuff.
        if (otherFaction.isRaidable() || otherFaction == hcfSession.getFaction())
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
            DesireHCF.getLangHandler().sendRenderMessage(hcfSession.getSession(), "factions.protection.use_items");
            return false;
        }

        // wilderness you can do anything
        else if (otherFaction.getType() == FactionType.WILDERNESS)
        {
            return true;
        }

        // relationship counseling
        FactionRelationship rel = otherFaction.getRelationshipTo(hcfSession.getFaction());
        if (!rel.canBuild())
        {
            DesireHCF.getLangHandler().sendRenderMessage(hcfSession.getSession(), "factions.protection.use_items");
            return false;
        }

        return true;
    }

}
