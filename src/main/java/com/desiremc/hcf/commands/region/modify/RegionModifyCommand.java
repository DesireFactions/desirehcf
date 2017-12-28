package com.desiremc.hcf.commands.region.modify;

import com.desiremc.core.api.newcommands.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class RegionModifyCommand extends ValidBaseCommand
{

    public RegionModifyCommand()
    {
        super("modify", "Modify the features of a region.", Rank.ADMIN, new String[] { "edit" });
        
        addSubCommand(new RegionModifyNameCommand());
        addSubCommand(new RegionModifyMaterialCommand());
        addSubCommand(new RegionModifyDistanceCommand());
    }

}
