package com.desiremc.hcf.commands.fstat;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.newvalidators.SenderHasFactionValidator;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.Faction;

public class FStatShowCommand extends FactionValidCommand
{

    public FStatShowCommand()
    {
        super("show", "Show my fstats");

        addSenderValidator(new SenderHasFactionValidator());
    }

    @Override
    public void validFactionRun(HCFSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        
        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "faction",
                "{faction}", faction);
        
        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "trophy_points",
                "{points}", Integer.toString(faction.getTrophies()));
        
        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "koth_wins",
                "{koth_wins}", Integer.toString(faction.getKothWins()));
    }

}
