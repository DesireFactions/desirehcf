package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderHasAmountMoney;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

import java.util.List;

public class FactionDepositCommand extends FactionValidCommand
{
    public FactionDepositCommand()
    {
        super("deposit", "Deposit money to your faction.", true);

        addSenderValidator(new SenderHasFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new IntegerParser())
                .addValidator(new SenderHasAmountMoney())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        double amount = ((Number) arguments.get(0).getValue()).doubleValue();

        faction.depositBalance(amount);
        sender.withdrawBalance(amount);

        faction.save();
        sender.save();

        faction.broadcast(DesireHCF.getLangHandler().renderMessage("factions.deposit", true, "{player}", sender.getName(), "{amount}", StringUtils.doubleFormat(amount)));
    }
}