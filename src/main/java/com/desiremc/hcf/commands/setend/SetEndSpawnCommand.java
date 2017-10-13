package com.desiremc.hcf.commands.setend;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.api.SetEndAPI;

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
