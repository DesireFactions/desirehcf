package com.desiremc.hcf.listener.factions;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.events.PlayerBlockMoveEvent;
import com.desiremc.core.utils.BlockColumn;
import com.desiremc.hcf.newvalidators.SenderClaimHasPointOneValidator;
import com.desiremc.hcf.newvalidators.SenderClaimingValidator;
import com.desiremc.hcf.newvalidators.SenderFactionOfficerValidator;
import com.desiremc.hcf.newvalidators.SenderHasFactionValidator;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.faction.ClaimSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.util.FactionsUtils;

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
        Faction faction = FactionsUtils.getFaction(event.getTo());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(event.getPlayer().getUniqueId());
        if (event.hasItem())
        {
            ItemStack item = event.getItem();
            if (FactionHandler.isClaimWand(item))
            {
                processClaim(session, event);
            }
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

        // if they left click a block with the wand
        if (event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            claim.setPointOne(new BlockColumn(block.getLocation().getBlockX(), block.getLocation().getBlockZ(), block.getLocation().getWorld()));
        }
        // if they right click a block with the wand
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (new SenderClaimHasPointOneValidator().factionsValidate(session))
            {
                claim.setPointTwo(new BlockColumn(block.getLocation().getBlockX(), block.getLocation().getBlockZ(), block.getLocation().getWorld()));
            }
        }
        // if they left click the air while sneaking
        else if (event.getAction() == Action.LEFT_CLICK_AIR && session.getPlayer().isSneaking())
        {
            // TODO check the claim
        }

    }

}
