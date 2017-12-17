package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.newvalidators.SenderHasFreeSlotValidator;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.ClaimSession;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;

import java.util.List;

public class FactionClaimCommand extends FactionValidCommand
{

    public FactionClaimCommand()
    {
        super("claim", "Claim land for your faction.", true, new String[] { "startclaim", "claimwand" });

        addSenderValidator(new SenderFactionOfficerValidator());
        addSenderValidator(new SenderHasFreeSlotValidator());
    }

    @Override
    public void validFactionRun(HCFSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        sender.getPlayer().getInventory().addItem(FactionHandler.getClaimWand());

        ClaimSession claimSession = new ClaimSession(sender);

        sender.setClaimSession(claimSession);

        claimSession.run();
    }

}
