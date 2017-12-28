package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HKit;
import com.desiremc.hcf.session.HKitHandler;

public class UnusedKitNameValidator implements Validator<String>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, String arg)
    {
        HKit kit = HKitHandler.getKit(arg);

        if (kit != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.used_name", true, false);
            return false;
        }

        return true;
    }

}
