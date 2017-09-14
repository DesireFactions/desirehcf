package com.desiremc.hcf.command.commands.lives;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.validator.PlayerIsBannedValidator;

public class LivesUseCommand extends ValidCommand
{

    public LivesUseCommand()
    {
        super("use", "Use a life to revive another player.", Rank.GUEST, new String[] { "player" });
        addParser(new PlayerSessionParser(), "player");
        addValidator(new PlayerIsBannedValidator(), "player");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        // TODO Auto-generated method stub
        
    }

}
