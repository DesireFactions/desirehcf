package com.desiremc.hcf.commands;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.npc.SafeLogoutTask;
import com.desiremc.hcf.util.FactionsUtils;
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
        Player player = (Player) sender;

        if (SafeLogoutTask.hasTask(player))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "logout.cancelled");
            SafeLogoutTask.cancel(player);
        }
        else
        {
            if(FactionsUtils.isInSafeZone(player))
            {
                player.kickPlayer(DesireHCF.getLangHandler().renderMessageNoPrefix("logout.success"));
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(sender, "logout.started");
                SafeLogoutTask.run(DesireHCF.getInstance(), player);
            }
        }

    }

}
