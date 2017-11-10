package com.desiremc.hcf.commands;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.npc.SafeLogoutTask;
import com.desiremc.hcf.validator.PlayerIsNotTaggedValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogoutCommand extends ValidCommand
{

    public LogoutCommand()
    {
        super("logout", "Start to safely logout", Rank.GUEST, new String[]{});

        addValidator(new PlayerValidator());
        addValidator(new PlayerIsNotTaggedValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        if (SafeLogoutTask.hasTask(p))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "logout.cancelled");
            SafeLogoutTask.cancel(p);
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "logout.started");
            SafeLogoutTask.run(DesireHCF.getInstance(), p);
        }

    }

}
