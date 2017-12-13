package com.desiremc.hcf.api.commands;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;

public abstract class FactionSenderValidator implements SenderValidator
{

    @Override
    public final boolean validate(Session sender)
    {
        HCFSession hcfSession = HCFSessionHandler.getHCFSession(sender.getUniqueId());
        
        return factionsValidate(hcfSession);
    }
    
    public abstract boolean factionsValidate(HCFSession sender);
    
}
