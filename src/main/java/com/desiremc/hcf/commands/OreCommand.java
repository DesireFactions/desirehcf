package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.gui.OreMenu;
import com.desiremc.hcf.session.HCFSessionHandler;

public class OreCommand extends ValidCommand
{

    public OreCommand()
    {
        super("ore", "View the ore you have mined.", Rank.GUEST, new String[] {}, new String[] { "ores" });

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        Menu menu = OreMenu.getOreMenu(HCFSessionHandler.getHCFSession(player.getUniqueId()));
        menu.openMenu(player);
    }

}
