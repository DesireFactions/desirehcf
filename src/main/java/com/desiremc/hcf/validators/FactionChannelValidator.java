package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionChannel;

public class FactionChannelValidator extends FactionValidator<FactionChannel>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FactionChannel arg)
    {
        if (arg == null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.chat.invalid_channel");
            return false;
        }

        return true;
    }
}
