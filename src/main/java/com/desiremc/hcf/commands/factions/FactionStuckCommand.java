package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderLandIsNotOwnValidator;
import com.desiremc.hcf.validators.SenderLandIsNotWildernessValidator;

public class FactionStuckCommand extends FactionValidCommand
{

    public FactionStuckCommand()
    {
        super("Stuck", "Teleport you to a safe location.");

        addSenderValidator(new SenderLandIsNotOwnValidator());
        addSenderValidator(new SenderLandIsNotWildernessValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (!FactionHandler.isStuck(sender))
        {

            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.stuck.start", true, false);   
            FactionHandler.setStuck(sender, true);
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.stuck.cancelled.command", true, false);
            FactionHandler.setStuck(sender, false);
        }
    }

}
