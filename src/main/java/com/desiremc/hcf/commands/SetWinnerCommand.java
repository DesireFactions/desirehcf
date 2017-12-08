package com.desiremc.hcf.commands;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.parser.FactionSessionParser;
import com.desiremc.hcf.session.FactionSession;
import com.desiremc.hcf.util.FactionsUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import org.bukkit.command.CommandSender;

public class SetWinnerCommand extends ValidCommand
{
    public SetWinnerCommand()
    {
        super("setwinner", "Set this seasons winner.", Rank.ADMIN, new String[] { "faction" });

        addParser(new FactionSessionParser(), "faction");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        FactionSession session = (FactionSession) args[0];
        Faction faction = FactionsUtils.getFaction(session.getName());

        for (FPlayer player : faction.getFPlayers())
        {
            Session s = SessionHandler.getSession(player.getPlayer());
            if (!s.hasAchievement(Achievement.FIRST_SEASON_WIN))
            {
                s.awardAchievement(Achievement.FIRST_SEASON_WIN, true);
            }
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "set_winner", "{faction}", session.getName());
    }
}
