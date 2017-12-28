package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.gui.EditKitMenu;
import com.desiremc.hcf.parsers.KitParser;
import com.desiremc.hcf.session.HKit;

import java.util.List;

public class KitManagementEditCommand extends ValidCommand
{

    public KitManagementEditCommand()
    {
        super("edit", "Edit the kit itself.", Rank.ADMIN, true, new String[] {"kit"});

        addArgument(CommandArgumentBuilder.createBuilder(HKit.class)
                .setName("kit")
                .setParser(new KitParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        HKit kit = (HKit) args.get(0).getValue();
        EditKitMenu menu = new EditKitMenu(kit);
        menu.initialize();
        menu.openMenu(sender.getPlayer());
    }

}
