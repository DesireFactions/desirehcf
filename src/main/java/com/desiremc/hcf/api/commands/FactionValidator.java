package com.desiremc.hcf.api.commands;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

public abstract class FactionValidator<T> implements Validator<T>
{

    @Override
    public final boolean validateArgument(Session sender, String[] label, T arg)
    {
        FSession hcfSession = FSessionHandler.getFSession(sender.getUniqueId());

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
    public abstract boolean factionsValidateArgument(FSession sender, String[] label, T arg);

}
