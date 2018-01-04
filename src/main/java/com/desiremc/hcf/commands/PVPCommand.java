package com.desiremc.hcf.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.PlayerHasSafeTimeLeft;

public class PVPCommand extends FactionValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new PlayerHasSafeTimeLeft());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("enable")
                .setParser(new StringParser())
                .setOptional()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String label[], List<CommandArgument<?>> args)
    {
        if (!args.get(0).hasValue() || !((String) args.get(0).getValue()).equalsIgnoreCase("enable"))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "pvp.time", true, false, "{time}", sender.getSafeTimer().getTimeLeftFormatted());
        }
        else
        {
            sender.setSafeTimeLeft(0);
            sender.save();
            DesireHCF.getLangHandler().sendRenderMessage(sender, "pvp.disabled", true, false);
            EntryRegistry.getInstance().removeValue(sender.getPlayer(), DesireHCF.getLangHandler().renderMessage("pvp.scoreboard", false, false));
        }
    }

}
