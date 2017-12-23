package com.desiremc.hcf.commands.region.modify;

import java.util.List;

import org.bukkit.material.MaterialData;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.MaterialDataParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.core.validators.ItemBlockValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.RegionParser;
import com.desiremc.hcf.session.Region;

public class RegionModifyMaterialCommand extends ValidCommand
{

    public RegionModifyMaterialCommand()
    {
        super("material", "Change the material of a region.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Region.class)
                .setName("region")
                .setParser(new RegionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(MaterialData.class)
                .setName("material")
                .setParser(new MaterialDataParser())
                .addValidator(new ItemBlockValidator())
                .build());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Region region = (Region) arguments.get(0).getValue();
        MaterialData data = (MaterialData) arguments.get(1).getValue();

        MaterialData oldData = new MaterialData(region.getBarrierMaterial(), (byte) region.getBarrierMaterialData());

        region.setBarrierMaterial(data);
        region.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.change",
                "{change}", "material",
                "{region}", region.getName(),
                "{old}", ItemNames.lookup(oldData),
                "{new}", ItemNames.lookup(data));

    }

}