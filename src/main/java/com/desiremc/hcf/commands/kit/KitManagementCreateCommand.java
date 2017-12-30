package com.desiremc.hcf.commands.kit;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PositiveIntegerParser;
import com.desiremc.core.parsers.RankParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;
import com.desiremc.hcf.validators.UnusedKitNameValidator;

public class KitManagementCreateCommand extends ValidCommand
{

    public KitManagementCreateCommand()
    {
        super("create", "Create a new kit.", Rank.ADMIN, true, new String[] {"new"});

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new UnusedKitNameValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("cooldown")
                .setParser(new PositiveIntegerParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Rank.class)
                .setName("rank")
                .setParser(new RankParser())
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        String name = (String) args.get(0).getValue();
        int cooldown = ((Number) args.get(1).getValue()).intValue();

        HKit kit;
        if (!args.get(2).hasValue())
        {
            kit = HKitHandler.createKit(name, cooldown);
        }
        else
        {
            kit = HKitHandler.createKit(name, cooldown, (Rank) args.get(2).getValue());
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.created", true, false,
                "{kit}", kit.getName());
    }

}
