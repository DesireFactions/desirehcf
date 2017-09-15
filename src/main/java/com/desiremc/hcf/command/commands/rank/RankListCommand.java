package com.desiremc.hcf.command.commands.rank;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.api.RankAPI;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;

/**
 * @author Ryan Radomski
 */
public class RankListCommand extends ValidCommand
{

    public RankListCommand()
    {
        super("list", "list all the ranks", Rank.GUEST, new String[]{});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        RankAPI.listRanks(sender);
    }

}
