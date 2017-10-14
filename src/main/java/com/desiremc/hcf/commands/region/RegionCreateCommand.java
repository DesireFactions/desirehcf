package com.desiremc.hcf.commands.region;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.MaterialDataParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.core.validators.ItemBlockValidator;
import com.desiremc.core.validators.StringLengthValidator;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionBlocks;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.validator.SelectedAreaValidator;
import com.desiremc.hcf.validator.UnusedRegionNameValidator;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class RegionCreateCommand extends ValidCommand
{

    public RegionCreateCommand()
    {
        super("create", "Create a new region.", Rank.ADMIN, new String[] { "name", "material" }, "new");
        addParser(new StringParser(), "name");
        addParser(new MaterialDataParser(), "material");

        addValidator(new StringLengthValidator(1, HCFCore.getConfigHandler().getInteger("regions.max-name")), "name");
        addValidator(new SelectedAreaValidator());
        addValidator(new ItemBlockValidator(), "material");
        addValidator(new UnusedRegionNameValidator(), "name");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        Selection s = HCFCore.getWorldEdit().getSelection(p);

        String name = (String) args[0];
        MaterialData data = (MaterialData) args[1];

        Region r = new Region(name, s.getWorld().getName(), new RegionBlocks(s.getMaximumPoint(), s.getMinimumPoint()), data, DesireCore.getConfigHandler().getInteger("barrier.view-distance"));
        RegionHandler.getInstance().save(r, true);

        HCFCore.getLangHandler().sendRenderMessage(sender, "region.create", "{name}", name, "{material}", ItemNames.lookup(data));
    }

}
