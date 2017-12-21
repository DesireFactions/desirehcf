package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidator;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionRank;

public class SenderCanDemoteTargetValidator extends FactionValidator<FSession>
{
    @Override
    public boolean factionsValidateArgument(FSession sender, String[] label, FSession arg)
    {
        if (sender.getFactionRank().ordinal() <= arg.getFactionRank().ordinal())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.demote.invalid");
            return false;
        }

        if (FactionRank.MEMBER.equals(arg.getFactionRank()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.demote.member", "{target}", arg.getName());
            return false;
        }

        return true;
    }
}