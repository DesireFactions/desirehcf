package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class FactionNotNeutralValidator extends FactionValidator<Faction>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, Faction arg)
    {
        Faction faction = sender.getFaction();
        if (!faction.getAllies().contains(arg))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.neutral.already_neutral", true, false, "{faction}", arg.getName());
            return false;
        }
        return true;
    }
}
