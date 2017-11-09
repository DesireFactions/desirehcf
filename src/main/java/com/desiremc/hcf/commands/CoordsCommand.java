package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;

public class CoordsCommand extends ValidCommand
{

    public CoordsCommand()
    {
        super("coords", "List of all important coordinates.", Rank.GUEST, new String[] {}, new String[] { "coordinates" });
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        for (String str : DesireHCF.getLangHandler().getStringList("coords"))
        {
            sender.sendMessage(str);
        }
    }

}
