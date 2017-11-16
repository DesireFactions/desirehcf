package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;

public class StartOfTheWorldCommand extends ValidCommand
{

    public StartOfTheWorldCommand()
    {
        super("startitup", "Triggers the start of the world.", Rank.DEVELOPER, new String[] { "confirm" }, new String[] { "siu" });
        // TODO Auto-generated constructor stub
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        // TODO Auto-generated method stub

    }

}
