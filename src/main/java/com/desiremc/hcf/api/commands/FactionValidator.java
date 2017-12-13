package com.desiremc.hcf.api.commands;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;

public abstract class FactionValidator<T> implements Validator<T>
{

    @Override
    public final boolean validateArgument(Session sender, String[] label, T arg)
    {
        HCFSession hcfSession = HCFSessionHandler.getHCFSession(sender.getUniqueId());

        return factionsValidateArgument(hcfSession, label, arg);
    }

    /**
     * A convenience method to use instead of validateArgument. FactionValidator will automatically convert Session to
     * HCFSession and then return the result of this method.
     * 
     * @param sender the sender of the command.
     * @param label the label of the command
     * @param arg the argument to be validated.
     * @return {@code true} if the argument is valid. {@code false} of the argument is not valid.
     */
    public abstract boolean factionsValidateArgument(HCFSession sender, String[] label, T arg);

}
