package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.npc.SafeLogoutTask;

public class LogoutCommand extends ValidCommand
{

    public LogoutCommand()
    {
        super("logout", "Start to safely logout", Rank.GUEST, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        if (SafeLogoutTask.hasTask(p))
        {
            HCFCore.getLangHandler().sendRenderMessage(sender, "logout.cancelled");
            SafeLogoutTask.cancel(p);
        }
        else
        {
            HCFCore.getLangHandler().sendRenderMessage(sender, "logout.started");
            SafeLogoutTask.run(HCFCore.getInstance(), p);
        }

    }

}
