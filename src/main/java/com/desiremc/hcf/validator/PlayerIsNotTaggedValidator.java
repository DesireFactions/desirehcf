package com.desiremc.hcf.validator;

import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerIsNotTaggedValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Player p = (Player) sender;

        if (TagHandler.getTag(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "tagged");
            return false;
        }
        return true;
    }

}
