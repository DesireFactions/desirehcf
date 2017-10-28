package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class MinerListener implements DesireClass
{

    @Override
    public void initialize()
    {
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.MINER.equals(session.getPvpClass()))
            return;

        if (!event.getBlock().getType().equals(Material.DIAMOND_ORE))
            return;

        session.addDiamonds(1);
    }
}
