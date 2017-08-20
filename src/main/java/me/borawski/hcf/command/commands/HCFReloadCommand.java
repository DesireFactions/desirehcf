package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.LangHandler;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;

public class HCFReloadCommand extends ValidCommand {

    public HCFReloadCommand() {
        super("hcfreload", "Reload the lang file.", Rank.ADMIN, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        LangHandler.reloadAll();
        LANG.sendString(sender, "hcfreload");
    }

}
