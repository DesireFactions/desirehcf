package com.desiremc.hcf.commands.fstat;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

public class FStatShowCommand extends FactionValidCommand
{

    public FStatShowCommand()
    {
        super("show", "Show my fstats");

        addSenderValidator(new SenderHasFactionValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "faction", true, false,
                "{faction}", faction.getName());

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "trophy_points", true, false,
                "{points}", Double.toString(faction.getTrophyPoints()));

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "koth_wins", true, false,
                "{koth_wins}", Integer.toString(faction.getKothWins()));
    }

}
