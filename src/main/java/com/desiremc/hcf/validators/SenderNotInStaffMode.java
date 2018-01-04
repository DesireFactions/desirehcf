package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.hcf.DesireHCF;

public class SenderNotInStaffMode implements SenderValidator
{
    @Override
    public boolean validate(Session sender)
    {
        if (StaffHandler.getInstance().inStaffMode(sender.getPlayer()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "no_staff", true, false);
            return false;
        }
        return false;
    }
}
