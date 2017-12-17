package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.barrier.TagHandler;
import org.bukkit.entity.Player;

public class PlayerIsNotTaggedValidator implements SenderValidator
{

    @Override
    public final boolean validate(Session sender)
    {
        Player p = sender.getPlayer();

        if (TagHandler.getTag(p.getUniqueId()) != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getPlayer(), "tagged");
            return false;
        }
        return true;
    }

}
