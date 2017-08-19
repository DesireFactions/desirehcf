package me.borawski.hcf.command.commands;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.command.commands.sub.FStatFactionCommand;
import me.borawski.hcf.command.commands.sub.FStatShowCommand;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 5/7/2017.
 */
public class FStatCommand extends ValidBaseCommand {

    public FStatCommand() {
        super("fstat", "View your player stats", Rank.GUEST,
                new String[] {});
        addSubCommand(new FStatShowCommand());
        addSubCommand(new FStatFactionCommand());
    }
}
