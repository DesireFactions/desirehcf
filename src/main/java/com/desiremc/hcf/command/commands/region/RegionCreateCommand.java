package com.desiremc.hcf.command.commands.region;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionBlocks;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.util.ItemNames;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class RegionCreateCommand extends ValidCommand
{

    public RegionCreateCommand()
    {
        super("create", "Create a new region.", Rank.ADMIN, new String[] { "name", "material" }, "new");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        Selection s = DesireCore.getWorldEdit().getSelection(p);
        if (s == null)
        {
            sender.sendMessage(LANG.getString("create.no-selection"));
            return;
        }

        String name = (String) args[0];
        if (name.length() > DesireCore.getConfigHandler().getInteger("regions.max-name"))
        {
            sender.sendMessage(LANG.getString("create.too-long"));
            return;
        }

        Region r = RegionHandler.getInstance().getRegion(name.toLowerCase());
        if (r != null)
        {
            sender.sendMessage(LANG.getString("create.name-taken"));
            return;
        }

        // TODO CHECK FOR BAD NAME
        ItemStack is = DesireCore.getItemHandler().get((String) args[1]);
        if (is == null)
        {
            sender.sendMessage(LANG.getString("create.invalid-item"));
            return;
        }
        if (is.getType().getId() > 217)
        {
            sender.sendMessage(LANG.getString("create.not-block"));
            return;
        }

        r = new Region(name, p.getWorld().getName(), new RegionBlocks(s.getMaximumPoint(), s.getMinimumPoint()), is.getData(), DesireCore.getConfigHandler().getInteger("barrier.view-distance"));
        RegionHandler.getInstance().save(r, true);
        sender.sendMessage(LANG.getString("create.success").replace("{name}", name).replace("{material}", ItemNames.lookup(is)));
    }

}
