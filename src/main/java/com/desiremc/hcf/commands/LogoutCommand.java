package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.tasks.SafeLogoutTask;
import com.desiremc.hcf.util.FactionsUtils;
import com.desiremc.hcf.validators.PlayerIsNotTaggedValidator;
import org.bukkit.entity.Player;

import java.util.List;

public class LogoutCommand extends ValidCommand
{

    public LogoutCommand()
    {
        super("logout", "Start to safely logout", Rank.GUEST, true, new String[] {});

        addSenderValidator(new PlayerIsNotTaggedValidator());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = sender.getPlayer();

        if (SafeLogoutTask.hasTask(player))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "logout.cancelled", true, false);
            SafeLogoutTask.cancel(player);
        }
        else
        {
            if (FactionsUtils.isInSafeZone(player))
            {
                player.kickPlayer(DesireHCF.getLangHandler().renderMessage("logout.success", false, false));
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(sender, "logout.started", true, false);
                SafeLogoutTask.run(DesireHCF.getInstance(), player);
            }
        }

    }

}
