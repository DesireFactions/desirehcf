package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

import java.util.List;

public class SetWinnerCommand extends FactionValidCommand
{
    public SetWinnerCommand()
    {
        super("setwinner", "Set this seasons winner.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = (Faction) arguments.get(0).getValue();

        for (FSession player : faction.getMembers())
        {
            if (!player.hasAchievement(Achievement.FIRST_SEASON_WIN))
            {
                player.awardAchievement(Achievement.FIRST_SEASON_WIN, true);
            }
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "set_winner", true, false, "{faction}", faction.getName());
    }
}
