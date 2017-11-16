package com.desiremc.hcf.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.OreData;

public class OreMenu extends Menu
{

    @SuppressWarnings("unused")
    private OreData data;

    private OreMenu(OreData data)
    {
        super("Ore History", 2);
        this.data = data;

        addMenuItem(new MenuItem("§a§lEmeralds§a: " + data.getEmeraldCount(), new MaterialData(Material.EMERALD))
        {
            @Override
            public void onClick(Player player)
            {
            }
        }, 2, 0);
        addMenuItem(new MenuItem("§b§lDiamonds§b: " + data.getDiamondCount(), new MaterialData(Material.DIAMOND))
        {
            @Override
            public void onClick(Player player)
            {
            }
        }, 3, 0);
        addMenuItem(new MenuItem("§6§lGold§6: " + data.getGoldCount(), new MaterialData(Material.GOLD_INGOT))
        {
            @Override
            public void onClick(Player player)
            {
            }
        }, 4, 0);
        addMenuItem(new MenuItem("§7§lIron§7: " + data.getIronCount(), new MaterialData(Material.IRON_INGOT))
        {
            @Override
            public void onClick(Player player)
            {
            }
        }, 5, 0);
        addMenuItem(new MenuItem("§8§lCoal§8: " + data.getCoalCount(), new MaterialData(Material.COAL))
        {
            @Override
            public void onClick(Player player)
            {
            }
        }, 6, 0);
    }

    public static Menu getOreMenu(HCFSession session)
    {
        return new OreMenu(session.getCurrentOre());
    }

}
