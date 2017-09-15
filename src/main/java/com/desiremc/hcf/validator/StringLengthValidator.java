package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.DesireCore;

public class StringLengthValidator extends CommandValidator
{

    private int minLength;
    private int maxLength;

    public StringLengthValidator(int minLength, int maxLength)
    {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        int length = ((String) arg).length();

        if (length < minLength)
        {
            DesireCore.getLangHandler().sendString(sender, "string.too_short");
            return false;
        }
        if (length > maxLength)
        {
            DesireCore.getLangHandler().sendString(sender, "string.too_long");
            return false;
        }

        return true;
    }

}
