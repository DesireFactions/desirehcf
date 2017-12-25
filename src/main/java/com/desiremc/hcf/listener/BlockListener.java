package com.desiremc.hcf.listener;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSessionHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class BlockListener implements Listener
{

    private static final boolean DEBUG = false;

    private final static LinkedList<Material> ALWAYS = new LinkedList<>(Arrays.asList(Material.GOLD_ORE, Material.IRON_ORE));
    private final static LinkedList<Material> NON_SILK = new LinkedList<>(Arrays.asList(Material.EMERALD_ORE, Material.DIAMOND_ORE, Material.COAL_ORE));

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOreBreak(BlockBreakEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        if (DEBUG)
        {
            System.out.println("BlockListener.onOreBreak() fired.");
        }
        Player p = event.getPlayer();
        Material type = event.getBlock().getType();

        if ((ALWAYS.contains(type) || (!p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH) && NON_SILK.contains(type))))
        {
            if (DEBUG)
            {
                System.out.println("BlockListener.onOreBreak() processing current ore.");
            }
            try
            {
                FSessionHandler.getOnlineFSession(p.getUniqueId()).getCurrentOre().add(type, 1);
            }
            catch (Exception ex)
            {
                ChatUtils.sendStaffMessage(ex, DesireHCF.getInstance());
            }
        }

        if (!DesireHCF.getConfigHandler().getStringList("xray_ores").contains(event.getBlock().getType().name().toLowerCase()))
        {
            return;
        }

        Set<Block> vein = getVein(event.getBlock());

        for (Session session : SessionHandler.getOnlineStaff())
        {
            if (session.getSetting(SessionSetting.FINDORE))
            {
                DesireHCF.getLangHandler().sendRenderMessage(session, "findore.notification", true, false,
                        "{player}", p.getName(),
                        "{count}", vein.size(),
                        "{ore}", type.name().replaceAll("_", " "));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
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
