package com.desiremc.hcf.commands.kit;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.ItemUtils;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
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
            ViewKitsMenu menu = new ViewKitsMenu(session);
            menu.initialize();
            menu.openMenu(player);
        }
        else
        {
            HKit kit = (HKit) args[0];
            Collection<ItemStack> itemColl = kit.getContents();
            ItemStack[] items = ItemUtils.toArray(itemColl);

            System.out.println("Debug 1: " + itemColl.size() + " " + items.length);
            for (ItemStack item : items)
            {
                System.out.println("Debug 2: " + item.toString());
            }

            player.getInventory().addItem(ItemUtils.toArray(kit.getContents()));
            session.useKit(kit);

            DesireHCF.getLangHandler().sendRenderMessage(player, "kits.used_kit",
                    "{kit}", kit.getName());
        }
    }

}
