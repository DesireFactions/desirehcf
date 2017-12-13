package com.desiremc.hcf.commands.region;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.MaterialDataParser;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.newvalidators.ItemBlockValidator;
import com.desiremc.core.newvalidators.SelectedAreaValidator;
import com.desiremc.core.newvalidators.StringLengthValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.newvalidators.regions.UnusedRegionNameValidator;
import com.desiremc.hcf.session.RegionBlocks;
import com.desiremc.hcf.session.RegionHandler;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class RegionCreateCommand extends ValidCommand
{

    public RegionCreateCommand()
    {
        super("create", "Create a new region.", Rank.ADMIN, true, new String[] { "new" });

        addSenderValidator(new SelectedAreaValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new StringLengthValidator(1, DesireHCF.getConfigHandler().getInteger("regions.max-name")))
                .addValidator(new UnusedRegionNameValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(MaterialData.class)
                .setName("material")
                .setParser(new MaterialDataParser())
                .addValidator(new ItemBlockValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Player player = sender.getPlayer();
        Selection selection = DesireCore.getWorldEdit().getSelection(player);

        String name = (String) arguments.get(0).getValue();
        MaterialData barrierMaterialData = (MaterialData) arguments.get(1).getValue();

        RegionHandler.createRegion(name,
                selection.getWorld(),
                new RegionBlocks(selection.getMaximumPoint(), selection.getMinimumPoint()),
                barrierMaterialData,
                DesireHCF.getConfigHandler().getInteger("barrier.view-distance"));

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.create",
                "{name}", name,
                "{material}", ItemNames.lookup(barrierMaterialData));
    }

}
