package com.desiremc.hcf.command.commands.rank;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.api.RankAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

public class RankCheckCommand extends ValidCommand {

    public RankCheckCommand() {
        super("check", "Check your rank.", Rank.GUEST, new String[] {}, "show");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        RankAPI.checkRank(sender, label);
    }

}
