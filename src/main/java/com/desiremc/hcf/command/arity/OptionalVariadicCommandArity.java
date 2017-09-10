package com.desiremc.hcf.command.arity;

public class OptionalVariadicCommandArity implements CommandArity {

    @Override
    public boolean validateArity(int sentArgsLength, int commandArgsLength) {
        return sentArgsLength >= commandArgsLength - 1;
    }

}
