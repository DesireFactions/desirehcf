package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.gui.EditKitMenu;
import com.desiremc.hcf.parser.KitParser;
import com.desiremc.hcf.session.HKit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitManagementEditCommand extends ValidCommand
{

    public KitManagementEditCommand()
    {
        super("edit", "Edit the kit itself.", Rank.HELPER, new String[] {"kit"});

        addParser(new KitParser(), "kit");

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        HKit kit = (HKit) args[0];
        EditKitMenu menu = new EditKitMenu(kit);
        menu.initialize();
        menu.openMenu(player);
    }

}
