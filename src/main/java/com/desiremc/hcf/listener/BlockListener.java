package com.desiremc.hcf.listener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;

public class BlockListener
{
    @EventHandler
    public void onOreBreak(BlockBreakEvent event)
    {
        Player p = event.getPlayer();

        String name = StringUtils.capitalize(event.getBlock().getType().name().toLowerCase().replace("_", ""));

        if (!DesireHCF.getConfigHandler().getStringList("xray_ores").contains(event.getBlock().getType().name().toLowerCase()))
        {
            return;
        }

        Set<Block> vein = getVein(event.getBlock());

        for (Session session : SessionHandler.getInstance().getStaff())
        {
            if (session.getSetting(SessionSetting.FINDORE))
            {
                DesireHCF.getLangHandler().sendRenderMessage(session, "findore.notification",
                        "{player}", p.getName(),
                        "{count}", vein.size(),
                        "{ore}", name);
            }
        }
    }

    private Set<Block> getVein(Block block)
    {
        Set<Block> vein = new HashSet<>();
        vein.add(block);
        getVein(block, vein);
        return vein;
    }

    private void getVein(Block block, Set<Block> vein)
    {
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                for (int k = -1; k < 2; k++)
                {
                    Block relative = block.getRelative(i, j, k);
                    if (!vein.contains(relative) && block.equals(relative) && (i != 0 || j != 0 || k != 0))
                    {
                        vein.add(relative);
                        getVein(relative, vein);
                    }
                }
            }
        }
    }
}
