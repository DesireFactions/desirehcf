package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.newparsers.IntegerParser;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

import java.util.List;

public class FactionWithdrawCommand extends FactionValidCommand
{
    public FactionWithdrawCommand()
    {
        super("withdraw", "Withdraw money from your faction.", true);

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new IntegerParser())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        double amount = Double.parseDouble((String) arguments.get(0).getValue());

        faction.withdrawBalance(amount);
        sender.depositBalance(amount);

        faction.save();
        sender.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.withdraw", "{player}", sender.getName(), "{amount}", StringUtils.doubleFormat(amount)));
    }
}
