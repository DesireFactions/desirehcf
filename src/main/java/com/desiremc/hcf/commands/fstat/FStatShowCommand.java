package com.desiremc.hcf.commands.fstat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.session.FactionSession;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import com.desiremc.hcf.validator.PlayerHasFactionValidator;

public class FStatShowCommand extends ValidCommand
{

    public FStatShowCommand()
    {
        super("show", "show my fstats", Rank.GUEST, new String[]{});
        addValidator(new PlayerHasFactionValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        String faction = FactionsUtils.getFaction((Player) sender).getTag();
        FactionSession session = FactionSessionHandler.getFactionSession(faction);
        LANG.sendRenderMessage(sender, "faction", "{faction}", faction);
        LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
        LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));
    }

}
