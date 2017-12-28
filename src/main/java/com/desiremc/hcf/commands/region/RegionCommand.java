package com.desiremc.hcf.commands.region;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.commands.region.modify.RegionModifyCommand;

public class RegionCommand extends ValidBaseCommand
{

    public RegionCommand()
    {
        super("hregion", "Manage the protection regions.", Rank.ADMIN, new String[] { "hregions", "hrg" });
        
        addSubCommand(new RegionCreateCommand());
        addSubCommand(new RegionListCommand());
        addSubCommand(new RegionModifyCommand());
        addSubCommand(new RegionDeleteCommand());
    }

}
