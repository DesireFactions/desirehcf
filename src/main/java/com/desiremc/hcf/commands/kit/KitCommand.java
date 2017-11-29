package com.desiremc.hcf.commands.kit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.gui.ViewKitsMenu;
import com.desiremc.hcf.parser.KitParser;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.validator.PlayerKitOffCooldownValidator;
import com.desiremc.hcf.validator.PlayerKitRequiredRankValidator;

public class KitCommand extends ValidCommand
{

    public KitCommand()
    {
        super("kit", "View or use kits.", Rank.GUEST, ARITY_OPTIONAL, new String[] { "kit" }, new String[] { "kits" });

        addParser(new KitParser(), "kit");

        addValidator(new PlayerValidator());

        addOptionalExistsValidator(new PlayerKitOffCooldownValidator(), "kit");
        addOptionalExistsValidator(new PlayerKitRequiredRankValidator(), "kit");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        HCFSession session = HCFSessionHandler.getHCFSession(player.getUniqueId());
        if (args.length == 0)
        {
            new ViewKitsMenu(session).openMenu(player);
        }
        else
        {
            HKit kit = (HKit) args[0];
            player.getInventory().addItem(kit.getContents().toArray(new ItemStack[0]));
            session.useKit(kit);
        }
    }

}
