package com.desiremc.hcf.command.commands.region.modify;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

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
