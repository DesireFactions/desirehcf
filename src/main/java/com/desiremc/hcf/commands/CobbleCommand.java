package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.listener.PickupListener;

public class CobbleCommand extends ValidCommand
{

    public CobbleCommand()
    {
        super("cobble", "Disable picking up cobble.", Rank.GUEST, new String[] {});

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        if (PickupListener.toggleCobble(p.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(p, "cobble.disable");
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(p, "cobble.enable");
        }
    }

}
