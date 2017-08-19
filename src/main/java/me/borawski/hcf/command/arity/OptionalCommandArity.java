package me.borawski.hcf.command.arity;

public class OptionalCommandArity implements CommandArity {

    @Override
    public boolean validateArity(int sentArgsLength, int commandArgsLength) {
        return sentArgsLength == commandArgsLength || sentArgsLength == commandArgsLength - 1;
    }

}
