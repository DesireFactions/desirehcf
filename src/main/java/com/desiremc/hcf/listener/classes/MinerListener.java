package com.desiremc.hcf.listener.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import com.desiremc.core.session.PVPClass;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

public class MinerListener implements DesireClass
{

    @Override
    public void initialize()
    {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        Player p = event.getPlayer();
        FSession session = FSessionHandler.getFSession(p.getUniqueId());

        if (!PVPClass.MINER.equals(session.getPvpClass()))
        {
            return;
        }

        if (!event.getBlock().getType().equals(Material.DIAMOND_ORE))
        {
            return;
        }

        session.getCurrentOre().addDiamond(1);
    }
}
