package com.desiremc.hcf.command.commands.setend;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.api.SetEndAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerValidator;

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
