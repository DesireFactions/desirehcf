package com.desiremc.hcf.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.ItemInHandValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.validators.SenderNotInStaffMode;

public class RenameCommand extends ValidCommand
{
    public RenameCommand()
    {
        super("rename", "Rename an item in your hand.", Rank.PREMIER, true);

        addSenderValidator(new SenderNotInStaffMode());
        addSenderValidator(new ItemInHandValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .setVariableLength()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Player player = sender.getPlayer();
        String name = (String) args.get(0).getValue();

        ItemStack item = player.getItemInHand();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);

        player.updateInventory();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "rename", true, false,
                "{name}", ChatColor.translateAlternateColorCodes('&', name));
    }
}
