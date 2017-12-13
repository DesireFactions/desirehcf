package com.desiremc.hcf.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.newparsers.FactionParser;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.faction.Faction;

public class SetWinnerCommand extends FactionValidCommand
{
    public SetWinnerCommand()
    {
        super("setwinner", "Set this seasons winner.", Rank.ADMIN, new String[] { "faction" });

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .build());

    }

    @Override
    public void validFactionRun(HCFSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = (Faction) arguments.get(0).getValue();

        for (HCFSession player : faction.getMembers())
        {
            if (!player.hasAchievement(Achievement.FIRST_SEASON_WIN))
            {
                player.awardAchievement(Achievement.FIRST_SEASON_WIN, true);
            }
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "set_winner", "{faction}", faction.getName());
    }
}
