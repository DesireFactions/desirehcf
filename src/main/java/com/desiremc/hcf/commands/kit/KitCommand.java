package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.ItemUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.gui.ViewKitsMenu;
import com.desiremc.hcf.parsers.KitParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.validators.PlayerKitOffCooldownValidator;
import com.desiremc.hcf.validators.PlayerKitRequiredRankValidator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public class KitCommand extends FactionValidCommand
{

    public KitCommand()
    {
        super("kit", "View or use kits.", Rank.GUEST, true, new String[] {"kits"});

        addArgument(CommandArgumentBuilder.createBuilder(HKit.class)
                .setName("kit")
                .setParser(new KitParser())
                .setOptional()
                .addValidator(new PlayerKitOffCooldownValidator())
                .addValidator(new PlayerKitRequiredRankValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> args)
    {
        Player player = sender.getPlayer();
        if (!args.get(0).hasValue())
        {
            ViewKitsMenu menu = new ViewKitsMenu(sender);
            menu.initialize();
            menu.openMenu(player);
        }
        else
        {
            HKit kit = (HKit) args.get(0).getValue();
            Collection<ItemStack> itemColl = kit.getContents();
            ItemStack[] items = ItemUtils.toArray(itemColl);

            System.out.println("Debug 1: " + itemColl.size() + " " + items.length);
            for (ItemStack item : items)
            {
                System.out.println("Debug 2: " + item.toString());
            }

            player.getInventory().addItem(ItemUtils.toArray(kit.getContents()));
            sender.useKit(kit);

            DesireHCF.getLangHandler().sendRenderMessage(player, "kits.used_kit",
                    "{kit}", kit.getName());
        }
    }

}
