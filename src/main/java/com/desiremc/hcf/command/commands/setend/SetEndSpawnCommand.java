package com.desiremc.hcf.command.commands.setend;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.api.SetEndAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

public class SetEndSpawnCommand extends ValidCommand {

    public SetEndSpawnCommand() {
        super("spawn", "set end spawn", Rank.ADMIN, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        SetEndAPI.setEndSpawn(sender, "endspawn", "set_end.spawn");
    }

}
