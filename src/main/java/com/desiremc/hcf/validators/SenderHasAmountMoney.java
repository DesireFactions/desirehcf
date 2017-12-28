package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

import net.minecraft.util.com.google.common.primitives.Doubles;

public class SenderHasAmountMoney extends FactionValidator<String>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, String arg)
    {
        double amount;
        if (arg.equalsIgnoreCase("all"))
        {
            amount = sender.getBalance();
        }
        else
        {
            if (Doubles.tryParse(arg) == null)
            {
                DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.invalid_deposit", true, false);
                return false;
            }
            else
            {
                amount = Doubles.tryParse(arg);
            }
        }


        if (sender.getBalance() < amount)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.no_money", true, false);
            return false;
        }
        return true;
    }
}
