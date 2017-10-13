package com.desiremc.hcf.commands.region;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.api.LangHandler;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.validator.RegionsExistValidator;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class RegionListCommand extends ValidCommand
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

    public RegionListCommand()
    {
        super("list", "List all the regions created.", Rank.ADMIN, new String[]{});

        addValidator(new RegionsExistValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Collection<Region> regions = RegionHandler.getInstance().getRegions();
        StringBuilder sb = new StringBuilder();

        // I did this instead of a for int i because every .get with a
        // linked list goes through the entire list to get to that point in
        // the list. Don't change it over.
        int i = 0;
        for (Region r : regions)
        {
            sb.append(r.getName());
            if (i != regions.size() - 1)
            {
                sb.append(", ");
            }
            i++;
        }
        LANG.sendRenderMessage(sender, "region.list", "{regions}", sb.toString());
    }

}
