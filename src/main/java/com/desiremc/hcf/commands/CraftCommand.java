package com.desiremc.hcf.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class CraftCommand extends ValidCommand
{
    public CraftCommand()
    {
        super("craft", "Open a virtual crafting bench.", Rank.PREMIER, true, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = sender.getPlayer();

        player.openInventory(Bukkit.getServer().createInventory(null, InventoryType.WORKBENCH));
    }
}
