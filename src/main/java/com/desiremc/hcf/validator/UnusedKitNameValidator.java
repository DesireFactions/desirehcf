package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;

public class UnusedKitNameValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        String name = (String) arg;
        HKit kit = HKitHandler.getKit(name);

        if (kit != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.used_name");
            return false;
        }

        return true;
    }

}
