package com.desiremc.hcf.validator;

import java.util.Map;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.api.LangHandler;

/**
 * @author Ryan Radomski
 *
 */
public abstract class CommandValidator {

    private int[] argsToValdate;

    protected static final LangHandler LANG = Core.getLangHandler();

    /**
     * @param sender
     * @param label
     * @param arg
     * @return
     */
    public abstract boolean validateArgument(CommandSender sender, String label, Object arg);

    public void setArgsToValidate(Map<String, Integer> argsMap, String... argsToValidate) {
        this.argsToValdate = new int[argsToValidate.length];

        for (int i = 0; i < argsToValdate.length; i++) {
            this.argsToValdate[i] = argsMap.get(argsToValidate[i]);
        }
    }

    public boolean validate(CommandSender sender, String label, Object[] args) {
        if (argsToValdate == null) {
            throw new IllegalStateException("Did not call setArgsToValidateIndices yet.");
        }

        // support for validators not tied to any arguments
        if (argsToValdate.length == 0) {
            return validateArgument(sender, label, null);
        }

        for (int toValidate : argsToValdate) {
            if (!validateArgument(sender, label, args[toValidate])) {
                return false;
            }
        }

        return true;
    }

}