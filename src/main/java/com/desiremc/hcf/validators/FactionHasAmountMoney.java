package com.desiremc.hcf.validators;

import org.bukkit.Bukkit;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

import net.minecraft.util.com.google.common.primitives.Doubles;

public class FactionHasAmountMoney extends FactionValidator<String>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, String arg)
    {
        Faction faction = sender.getFaction();
        double amount;
        if (arg.equalsIgnoreCase("all"))
        {
            amount = faction.getBalance();
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


        if (faction.getBalance() < amount)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.no_money_faction", true, false);
            return false;
        }
        Bukkit.broadcastMessage(faction.getBalance() + ":" + amount);
        return true;
    }
}
