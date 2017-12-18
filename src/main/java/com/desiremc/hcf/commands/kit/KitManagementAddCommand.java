package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newvalidators.ItemInHandValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parsers.KitParser;
import com.desiremc.hcf.session.HKit;

import java.util.List;

public class KitManagementAddCommand extends ValidCommand
{

    public KitManagementAddCommand()
    {
        super("add", "Add an item to a kit.", Rank.ADMIN, true);

        addArgument(CommandArgumentBuilder.createBuilder(HKit.class)
                .setName("kit")
                .setParser(new KitParser())
                .addSenderValidator(new ItemInHandValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        HKit kit = (HKit) args.get(0).getValue();

        kit.addItem(sender.getPlayer().getItemInHand());
        kit.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.add_item",
                "{kit}", kit.getName());
    }

}
