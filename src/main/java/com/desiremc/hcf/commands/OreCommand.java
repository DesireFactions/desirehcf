package com.desiremc.hcf.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.gui.OreMenu;
import com.desiremc.hcf.session.FSession;

public class OreCommand extends FactionValidCommand
{

    public OreCommand()
    {
        super("ore", "View the ore you have mined.", Rank.GUEST, true, new String[] { "ores" });
    }

    @Override
    public void validFactionRun(FSession sender, String label[], List<CommandArgument<?>> args)
    {
        Menu menu = OreMenu.getOreMenu(sender);
        menu.openMenu(sender.getPlayer());
    }

}
