package me.borawski.hcf.command.commands.lives;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.session.Rank;

public class LivesCommand extends ValidBaseCommand {

    public LivesCommand() {
        super("lives", "Add or remove lives.", Rank.MODERATOR);
        addSubCommand(new LivesAddCommand());
        addSubCommand(new LivesRemoveCommand());
    }

}
