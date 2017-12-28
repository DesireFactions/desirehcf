package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionHasInvites;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

public class FactionInvitesCommand extends FactionValidCommand
{

    public FactionInvitesCommand()
    {
        super("invites", "List all outgoing invites from your faction.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());
        addSenderValidator(new SenderFactionHasInvites());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();

        StringBuilder sb = new StringBuilder();

        for (FSession player : faction.getInvites())
        {
            sb.append(player.getName());
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.invites.valid", true, false, "{players}", sb.toString());
    }
}
