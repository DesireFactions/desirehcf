package me.borawski.hcf.command.arity;

public class RequiredVariadicCommandArity implements CommandArity {

    @Override
    public boolean validateArity(int sentArgsLength, int commandArgsLength) {
        return sentArgsLength >= commandArgsLength;
    }

}
