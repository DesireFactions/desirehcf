package com.desiremc.hcf.commands;

import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;

public class HCFReloadCommand extends ValidCommand
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

    public HCFReloadCommand()
    {
        super("hcfreload", "Reload the lang file.", Rank.ADMIN, new String[]{});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        FileHandler.reloadAll();
        LANG.sendString(sender, "hcfreload");
    }

}
