package com.desiremc.hcf.commands.setend;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.api.SetEndAPI;

public class SetEndExitCommand extends ValidCommand {

    public SetEndExitCommand() {
        super("exit", "set end exit", Rank.ADMIN, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        SetEndAPI.setEndExit(sender, "endexit", "set_end.exit");
    }
}
