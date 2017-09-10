package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.api.FileHandler;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;

public class HCFReloadCommand extends ValidCommand {

    public HCFReloadCommand() {
        super("hcfreload", "Reload the lang file.", Rank.ADMIN, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        FileHandler.reloadAll();
        LANG.sendString(sender, "hcfreload");
    }

}
