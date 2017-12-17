package com.desiremc.hcf.commands.kit;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.RankParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.IntegerSizeValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;
import com.desiremc.hcf.validator.UnusedKitNameValidator;
import org.bukkit.command.CommandSender;

public class KitManagementCreateCommand extends ValidCommand
{

    public KitManagementCreateCommand()
    {
        super("create", "Create a new kit.", Rank.HELPER, ARITY_OPTIONAL, new String[] {"name", "cooldown", "rank"}, new String[] {"new"});

        addParser(new StringParser(), "name");
        addParser(new IntegerParser(), "cooldown");
        addParser(new RankParser(), "rank");

        addValidator(new UnusedKitNameValidator(), "name");
        addValidator(new IntegerSizeValidator(0, 2_592_000), "cooldown");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        String name = (String) args[0];
        int cooldown = (int) args[1];

        HKit kit;
        if (args.length == 2)
        {
            kit = HKitHandler.createKit(name, cooldown);
        }
        else
        {
            kit = HKitHandler.createKit(name, cooldown, (Rank) args[2]);
        }
        
        DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.created", 
                "{kit}", kit.getName());
    }

}
