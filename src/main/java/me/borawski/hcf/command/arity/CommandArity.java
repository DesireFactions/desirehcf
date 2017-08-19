package me.borawski.hcf.command.arity;

public interface CommandArity {

    public boolean validateArity(int sentArgsLength, int commandArgsLength);

}
