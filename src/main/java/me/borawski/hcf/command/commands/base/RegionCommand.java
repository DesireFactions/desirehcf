package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.command.commands.sub.RegionCreateCommand;
import me.borawski.hcf.command.commands.sub.RegionDeleteCommand;
import me.borawski.hcf.command.commands.sub.RegionListCommand;
import me.borawski.hcf.command.commands.sub.RegionModifyCommand;
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
