package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.gui.OreMenu;
import com.desiremc.hcf.session.HCFSessionHandler;

import java.util.List;

public class OreCommand extends ValidCommand
{

    public OreCommand()
    {
        super("ore", "View the ore you have mined.", Rank.GUEST, true, new String[] {"ores"});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Menu menu = OreMenu.getOreMenu(HCFSessionHandler.getHCFSession(sender.getUniqueId()));
        menu.openMenu(sender.getPlayer());
    }

}
