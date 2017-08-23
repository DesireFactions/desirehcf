package me.borawski.hcf.command.commands.region;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class RegionCommand extends ValidBaseCommand {

    public RegionCommand() {
        super("hregion", "Manage the protection regions.", Rank.ADMIN, "hregions", "hrg");
        addSubCommand(new RegionCreateCommand());
        addSubCommand(new RegionDeleteCommand());
        addSubCommand(new RegionListCommand());
        addSubCommand(new RegionModifyCommand());
    }

}
