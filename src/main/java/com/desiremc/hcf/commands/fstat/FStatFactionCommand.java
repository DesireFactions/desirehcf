package com.desiremc.hcf.commands.fstat;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.parser.FactionSessionParser;
import com.desiremc.hcf.session.FactionSession;

public class FStatFactionCommand extends ValidCommand
{

    public FStatFactionCommand()
    {
        super("faction", "shows faction stats", Rank.MODERATOR, new String[] { "faction" });
        addParser(new FactionSessionParser(), "faction");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        FactionSession session = (FactionSession) args[0];
        LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
        LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));
        LANG.sendRenderMessage(sender, "faction", "{faction}", session.getName());
    }

}
