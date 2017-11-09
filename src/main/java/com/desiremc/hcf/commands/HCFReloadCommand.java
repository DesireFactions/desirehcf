package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;

public class HCFReloadCommand extends ValidCommand
{

    public HCFReloadCommand()
    {
        super("hcfreload", "Reload the lang file.", Rank.ADMIN, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        DesireHCF.getLangHandler().reload();
        DesireHCF.getConfigHandler().reload();
        DesireHCF.getLangHandler().sendString(sender, "hcfreload");
    }

}
