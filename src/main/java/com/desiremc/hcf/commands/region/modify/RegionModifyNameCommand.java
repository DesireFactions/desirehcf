package com.desiremc.hcf.commands.region.modify;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.StringLengthValidator;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.parser.RegionParser;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.validator.UnusedRegionNameValidator;

public class RegionModifyNameCommand extends ValidCommand
{

    public RegionModifyNameCommand()
    {
        super("name", "Change the name of a region.", Rank.ADMIN, new String[] { "region", "name" });

        addParser(new RegionParser(), "region");
        addParser(new StringParser(), "name");

        addValidator(new UnusedRegionNameValidator(), "name");
        addValidator(new StringLengthValidator(1, HCFCore.getConfigHandler().getInteger("regions.max-name")), "name");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Region r = (Region) args[0];
        String name = (String) args[1];
        String oldName = r.getName();

        if (name.equals(oldName))
        {
            sender.sendMessage(HCFCore.getLangHandler().getString("region.same_name"));
            return;
        }

        r.setName(name);
        RegionHandler.getInstance().save(r);

        HCFCore.getLangHandler().sendRenderMessage(sender, "region.changed_name", "{change}", "name", "{old}", oldName, "{new}", name);

    }

}
