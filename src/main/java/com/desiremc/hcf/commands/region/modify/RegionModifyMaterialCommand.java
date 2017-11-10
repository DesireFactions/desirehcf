package com.desiremc.hcf.commands.region.modify;

import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.MaterialDataParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.ItemNames;
import com.desiremc.core.validators.ItemBlockValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.RegionParser;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionModifyMaterialCommand extends ValidCommand
{
    
    public RegionModifyMaterialCommand()
    {
        super("material", "Change the material of a region.", Rank.ADMIN, new String[] { "region", "material" }, new String[] {});

        addParser(new RegionParser(), "region");
        addParser(new MaterialDataParser(), "material");

        addValidator(new ItemBlockValidator(), "material");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Region r = (Region) args[0];
        MaterialData data = (MaterialData) args[1];
        MaterialData oldData = new MaterialData(r.getBarrierMaterial(), (byte) r.getBarrierMaterialData());

        r.setBarrierMaterial(data);
        RegionHandler.getInstance().save(r);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "region.change",
                "{change}", "material",
                "{region}", r.getName(),
                "{old}", ItemNames.lookup(oldData),
                "{new}", ItemNames.lookup(data));

    }

}