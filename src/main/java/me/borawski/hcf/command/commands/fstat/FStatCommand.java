package me.borawski.hcf.command.commands.fstat;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class FStatCommand extends ValidBaseCommand {

    public FStatCommand() {
        super("fstat", "View your player stats", Rank.GUEST,
                new String[] {});
        addSubCommand(new FStatShowCommand());
        addSubCommand(new FStatFactionCommand());
    }
}
