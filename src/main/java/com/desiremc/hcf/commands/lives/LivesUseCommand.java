package com.desiremc.hcf.commands.lives;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerIsBannedValidator;

public class LivesUseCommand extends ValidCommand
{

    public LivesUseCommand()
    {
        super("use", "Use a life to revive another player.", Rank.GUEST, new String[]{"player"});
        addParser(new PlayerSessionParser(), "player");
        addValidator(new PlayerIsBannedValidator(), "player");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        // TODO Auto-generated method stub

    }

}
