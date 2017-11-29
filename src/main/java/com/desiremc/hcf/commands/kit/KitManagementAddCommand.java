package com.desiremc.hcf.commands.kit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.ItemInHandValidator;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.KitParser;
import com.desiremc.hcf.session.HKit;

public class KitManagementAddCommand extends ValidCommand
{

    public KitManagementAddCommand()
    {
        super("add", "Add an item to a kit.", Rank.JRMOD, new String[] { "kit" });

        addParser(new KitParser(), "kit");

        addValidator(new PlayerValidator());
        addValidator(new ItemInHandValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        HKit kit = (HKit) args[0];

        kit.addItem(player.getItemInHand());
        kit.save();
        
        DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.add_item",
                "{kit}", kit.getName());
    }

}
