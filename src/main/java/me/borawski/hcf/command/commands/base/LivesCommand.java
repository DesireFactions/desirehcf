package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.commands.sub.LivesAddCommand;
import me.borawski.hcf.command.commands.sub.LivesRemoveCommand;
import me.borawski.hcf.session.Rank;

public class LivesCommand extends CustomBaseCommand {

    public LivesCommand() {
        super("lives", "Add or remove lives.", Rank.MODERATOR);
        addSubCommand(new LivesAddCommand());
        addSubCommand(new LivesRemoveCommand());
    }

}
