package com.desiremc.hcf.command.commands.region;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class RegionCommand extends ValidBaseCommand {

    public RegionCommand() {
        super("hregion", "Manage the protection regions.", Rank.ADMIN, "hregions", "hrg");
        addSubCommand(new RegionCreateCommand());
        addSubCommand(new RegionDeleteCommand());
        addSubCommand(new RegionListCommand());
        addSubCommand(new RegionModifyCommand());
    }

}
