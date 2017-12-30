package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.FactionHasAmountMoney;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

import net.minecraft.util.com.google.common.primitives.Doubles;

public class FactionWithdrawCommand extends FactionValidCommand
{
    public FactionWithdrawCommand()
    {
        super("withdraw", "Withdraw money from your faction.", true, new String[] {"w"});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("amount")
                .setParser(new StringParser())
                .addValidator(new FactionHasAmountMoney())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        String arg = (String) arguments.get(0).getValue();
        double amount;

        if (arg.equalsIgnoreCase("all"))
        {
            amount = sender.getBalance();
        }
        else
        {
            amount = Doubles.tryParse(arg);
        }

        faction.withdrawBalance(amount);
        sender.depositBalance(amount);

        faction.save();
        sender.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.withdraw", true, false, "{player}", sender.getName(), "{amount}", StringUtils.doubleFormat(amount)));
    }
}
