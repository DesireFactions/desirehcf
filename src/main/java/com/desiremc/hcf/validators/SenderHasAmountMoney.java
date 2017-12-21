package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;

public class SenderHasAmountMoney extends FactionValidator<Integer>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, Integer arg)
    {
        if (sender.getBalance() < arg)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.no_money");
            return false;
        }
        return true;
    }
}
